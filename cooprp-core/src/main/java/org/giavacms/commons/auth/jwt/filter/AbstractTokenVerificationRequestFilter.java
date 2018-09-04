package org.giavacms.commons.auth.jwt.filter;

import org.giavacms.commons.auth.jwt.util.JWTUtils;
import org.jboss.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

//@Provider
//@AccountTokenVerification
//@Priority(value = 1)
public abstract  class AbstractTokenVerificationRequestFilter implements ContainerRequestFilter
{

   @Context
   HttpServletRequest httpServletRequest;

   private final static Logger logger = Logger.getLogger(AbstractTokenVerificationRequestFilter.class.getName());

   @Override
   public void filter(ContainerRequestContext requestCtx) throws IOException
   {
//      logger.info("Executing REST AbstractTokenVerificationRequestFilter filter");
      if (requestCtx.getMethod().equals("OPTIONS"))
      {
//         logger.info("bypasso options");
         return;
      }

      try
      {
         String token = JWTUtils.getBearerToken(httpServletRequest);
         String jwtSecret = getJwtSecret();
//         logger.info("token: " + token + ", jwtSecret: " + jwtSecret);
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
//         logger.error("Error AbstractTokenVerificationRequestFilter " + e.getMessage());
         //         requestCtx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
         //         return;
      }
   }

   abstract protected String getJwtSecret();
}
