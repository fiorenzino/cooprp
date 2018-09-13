package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.pojo.Societa;
import it.coopservice.cooprp.repository.OperatoriAttiviRepository;
import it.coopservice.cooprp.repository.SocietaRepository;
import it.coopservice.cooprp.service.LdapService;
import org.giavacms.commons.auth.jwtcookie.annotation.AccountCookieAndTokenVerification;
import org.giavacms.commons.model.pojo.Utente;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Path(AppConstants.UTENTI_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountCookieAndTokenVerification
@Interceptors({ TracerInterceptor.class })
public class UtentiRepositoryRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected final Logger logger = Logger.getLogger(getClass());

   @Context
   HttpServletRequest httpServletRequest;

   @Inject
   LdapService ldapService;

   @Inject OperatoriAttiviRepository operatoriAttiviRepository;

   @Inject SocietaRepository societaRepository;

   @GET
   public Response identity()
   {
      try
      {
         String username = httpServletRequest.getUserPrincipal().getName();
         if (username == null)
         {
            return Response
                     .status(Response.Status.FORBIDDEN)
                     .entity("No user found for alias: " + username
                     ).build();
         }
         Utente utente = ldapService.find(username);

         List<Societa> societas = operatoriAttiviRepository.societaPagaOperatore(username);
         utente.societa = societas;

         //String matrProvv = operatoriAttiviRepository.matricolaProvvOperatoreValido(username);
         if (utente != null && !utente.getRuoli().isEmpty())
         {
            return Response.status(Response.Status.OK).entity(utente).build();
         }
         return Response
                  .status(Response.Status.FORBIDDEN)
                  .entity("No user found for alias: " + username
                  ).build();
      }
      catch (Exception e)
      {
         logger.error(e.getMessage());
         return Response
                  .status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity("Error attempting login: " + e.getMessage())
                  .build();
      }
   }

   @OPTIONS
   public Response options()
   {
      logger.info("@OPTIONS");
      return Response.ok().build();
   }

   @OPTIONS
   @Path("{path:.*}")
   public Response allOptions()
   {
      logger.info("@OPTIONS ALL");
      return Response.ok().build();
   }
}
