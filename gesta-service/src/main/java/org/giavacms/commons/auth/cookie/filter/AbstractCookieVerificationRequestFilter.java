package org.giavacms.commons.auth.cookie.filter;

import org.jboss.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

//@Provider
//@AccountCookieVerification
//@Priority(value = 2)
public class AbstractCookieVerificationRequestFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest httpServletRequest;


    private final static Logger logger = Logger.getLogger(AbstractCookieVerificationRequestFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestCtx) throws IOException {
        logger.info("Executing REST AbstractCookieVerificationRequestFilter filter");
        if (requestCtx.getMethod().equals("OPTIONS")) {
            logger.info("bypasso options");
            return;
        }

        try {
            if (requestCtx.getSecurityContext().getUserPrincipal() != null) {

            }

            if (httpServletRequest.getUserPrincipal() == null) {
                logger.error("Error AbstractCookieVerificationRequestFilter: user principal null ");
                requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

        } catch (Exception e) {
            logger.error("Error AbstractCookieVerificationRequestFilter " + e.getMessage());
            requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }
    }
}
