package org.giavacms.commons.auth.basic.filter;

import org.giavacms.commons.auth.basic.util.BasicUtils;
import org.giavacms.commons.util.Base64;
import org.jboss.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

//@Provider
//@BasicVerification
//@Priority(value = 2)
public abstract class AbstractBasicVerificationRequestFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest httpServletRequest;

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public void filter(ContainerRequestContext requestCtx) throws IOException {
        logger.info("Executing REST BasicVerificationRequestFilter filter");
        if (requestCtx.getMethod().equals("OPTIONS")) {
            logger.info("bypasso options");
            return;
        }

        try {
            String encodedUserPassword = BasicUtils.getBasic(httpServletRequest);
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword));
            try {
                if (httpServletRequest.getUserPrincipal() != null) {
                    httpServletRequest.logout();
                }
            } catch (ServletException e) {
                e.printStackTrace();
            }

            httpServletRequest.login(usernameAndPassword.split(":")[0], usernameAndPassword.split(":")[1]);
        } catch (Exception e) {
            requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }
    }
}
