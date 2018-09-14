package org.giavacms.commons.auth.cas.filter;

import org.giavacms.commons.auth.cas.util.CasUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @WebFilter(urlPatterns = {"*.jsf", "*.jsp", "/private/index.html"})
 */
public abstract class AbstractCasFilter implements Filter {

    String targetHost = "https://cas.coopservice.it";
    String targetPathValidate = "/cas/serviceValidate";

    public static final String TICKET_PARAMETER = "ticket";
    public static final String SERVICE_PARAMETER = "service";

    Logger logger = Logger.getLogger(getClass());

    private List<String> unprotectedResources = null;

    public void destroy() {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException,
            ServletException {
//        logger.info("do filter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpServletRequest.getRequestURI();

        try {

            if (requestURI.contains("logout.jsp")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // unprotected resources are not filtered
            if (unprotectedResources != null) {
                for (String unprotectedPattern : unprotectedResources) {
                    if (requestURI.startsWith(unprotectedPattern)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            }
//            logger.info("zona file protetti filter");
            // if principal is already present we do not filter anymore
            // credo non possa mai venire non nulla, prima di passare dai moduli --> si se viene specificato il
            // cache-type=default nella security-domain
            if (httpServletRequest.getUserPrincipal() != null && httpServletRequest.getUserPrincipal().getName() != null) {
                filterChain.doFilter(servletRequest, servletResponse);
                logger.info("httpServletRequest.getUserPrincipal(): " + httpServletRequest.getUserPrincipal());
                return;
            }
//            logger.info("principal nullo");
            // cerco il principal nella security association
//         if (SecurityContextAssociation.getPrincipal() != null
//                  && SecurityContextAssociation.getPrincipal().getName() != null)
//         {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//         }

            // if we are back from a CAS+TGT request we extract the ticket parameter to validate it against cas and thus
            // get
            // the user principal
            final String ticket = CasUtils.safeGetParameter(httpServletRequest,
                    TICKET_PARAMETER);

            final String casServiceCallbackUrl = CasUtils.getCasServiceCallbackUrl(httpServletRequest);
            if (ticket != null && ticket.trim().length() > 0) {
                String username = casServiceCallbackUrl;
                Form form = new Form();
                form.param("service", casServiceCallbackUrl);

                WebTarget target = getTarget(targetHost, targetPathValidate);
                String user = target.queryParam("ticket", ticket)
                        .request(MediaType.APPLICATION_JSON).post(
                                Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
                if (user != null && user.contains("<cas:user>")) {

                    username = user.substring(
                            user.indexOf("<cas:user>") + "<cas:user>".length(),
                            user.indexOf("</cas:user>"));
                }
                String password = ticket;
//                logger.info("username: " + username + ", pwd: " + password);
                httpServletRequest.login(username, password);
                if (httpServletRequest.getUserPrincipal() != null
                        && httpServletRequest.getUserPrincipal().getName() != null) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    // forbidden if login fails
                    httpServletResponse.sendError(403);
                }

            }

            // redirect to cas otherwise
            String urlToRedirectTo = CasUtils.getRedirectUrl(getCasServerLoginUrl(), SERVICE_PARAMETER,
                    casServiceCallbackUrl);
            httpServletResponse.sendRedirect(urlToRedirectTo);
        } catch (Throwable t) {
//            logger.info("filter Throwable: " + t.getMessage());
            // forbidden if login throws errors // httpServletResponse.sendError(500);
            httpServletResponse.sendError(403, t.getMessage());
        }
    }

    abstract protected String getCasServerLoginUrl();
    abstract protected String getCasExcluded();

    public void init(FilterConfig filterConfig) throws ServletException {

            this.unprotectedResources = new ArrayList<String>();
            for (String casExcludedPattern : getCasExcluded().split(";|,")) {
                this.unprotectedResources.add(filterConfig.getServletContext()
                        .getContextPath() + casExcludedPattern);
            }
    }

    public WebTarget getTarget(String targetHost, String targetPath) {
        try {
            Client client = new ResteasyClientBuilder().disableTrustManager()
                    .build();
            WebTarget target = client.target(targetHost + targetPath);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
