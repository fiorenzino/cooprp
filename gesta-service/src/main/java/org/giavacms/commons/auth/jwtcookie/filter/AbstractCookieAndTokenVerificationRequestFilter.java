package org.giavacms.commons.auth.jwtcookie.filter;

import org.giavacms.commons.auth.jwt.util.JWTUtils;
import org.jboss.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

//@Provider
//@AccountCookieAndTokenVerification
public abstract class AbstractCookieAndTokenVerificationRequestFilter implements ContainerRequestFilter
{

   @Context
   HttpServletRequest httpServletRequest;

   private final static Logger logger = Logger.getLogger(AbstractCookieAndTokenVerificationRequestFilter.class.getName());

   @Override
   public void filter(ContainerRequestContext requestCtx) throws IOException
   {
      logger.info("Executing REST TokenOrCookieVerificationRequestFilter filter");
      if (requestCtx.getMethod().equals("OPTIONS"))
      {
         logger.info("bypasso options");
         return;
      }

      try
      {
         logger.info("Cookie verificatrion");
         if (requestCtx.getSecurityContext().getUserPrincipal() != null)
         {
            logger.warn("Coookie verified");
            return;
         }

         String token = JWTUtils.getBearerToken(httpServletRequest);
         String jwtSecret = getJwtSecret();
         logger.info("token: " + token + ", jwtSecret: " + jwtSecret);
         try
         {
            if (httpServletRequest.getUserPrincipal() != null)
            {
               httpServletRequest.logout();
            }
         }
         catch (ServletException e)
         {
            e.printStackTrace();
         }

         httpServletRequest.login(token, jwtSecret);
      }
      catch (Exception e)
      {
         logger.error("Error TokenOrCookieVerificationRequestFilter " + e.getMessage());
         requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
         return;
      }
   }

   protected abstract String getJwtSecret();

}
