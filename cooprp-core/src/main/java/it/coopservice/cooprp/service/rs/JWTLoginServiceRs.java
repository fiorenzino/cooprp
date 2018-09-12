package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.service.LdapService;
import org.giavacms.commons.auth.jwt.model.pojo.JWTLogin;
import org.giavacms.commons.auth.jwt.model.pojo.JWTToken;
import org.giavacms.commons.auth.jwt.util.JWTUtils;
import org.giavacms.commons.model.pojo.Utente;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Path(AppConstants.LOGIN_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Interceptors({ TracerInterceptor.class })
public class JWTLoginServiceRs implements Serializable {

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @Context
   HttpServletRequest httpServletRequest;

   @Inject
   LdapService ldapService;


   private List<String> getJwtRoles() throws Exception {
      List<String> jwtRoles = new ArrayList<String>();
      String jwtRolesAsString = AppProperties.jwtRoles.value();
      if (jwtRolesAsString != null) {
         for (String jwtRole : jwtRolesAsString.split(";|,")) {
            jwtRoles.add(jwtRole.trim());
         }
      }
      return jwtRoles;
   }

   private String getJwtSecret() throws Exception {
      return AppProperties.jwtSecret.value();
   }

   private int getJwtExpireTime() throws Exception {
      return AppProperties.jwtExpireTime.cast(Integer.class);
   }

   @GET
   public Response get(@QueryParam("username") String username, @QueryParam("password") String password) {
      return post(new JWTLogin(username, password));
   }

   @POST
   public Response post(JWTLogin jwtLogin) {
      Utente utente = null;
      try {
         if (httpServletRequest.getUserPrincipal() != null) {
            httpServletRequest.logout();
         }
      } catch (ServletException e) {
         e.printStackTrace();
      }
      try {
         utente = ldapService.login(jwtLogin.getUsername(), jwtLogin.getPassword());
         if (utente == null) {
            throw new Exception("utente is null");
         }
         logger.info("jwtLogin: " + jwtLogin);

      } catch (Throwable e) {
         System.out.println("forbidden if login fails: " + e.getMessage());
         // forbidden if login fails
         return Response
                  .status(Response.Status.FORBIDDEN)
                  .entity("Login failed for user: " + jwtLogin
                  ).build();
      }

      try {

         List<String> userRoles = new ArrayList<String>();
         for (String jwtRole : getJwtRoles()) {
            if (utente.getRuoli().contains(jwtRole)) {
               userRoles.add(jwtRole);
            }
         }

         String token = JWTUtils.encode(getJwtSecret(), getJwtExpireTime(),
                  jwtLogin.getUsername(), jwtLogin.getPassword(), userRoles);

         // return Response.status(Response.Status.OK).entity("{\"token\":\"" + token + "\"}").build();
         return Response.status(Response.Status.OK).entity(new JWTToken(token)).build();
      } catch (Throwable e) {
         logger.error(e.getMessage());
         return Response
                  .status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity("Error attempting login: " + e.getMessage())
                  .build();
      }
   }

}
