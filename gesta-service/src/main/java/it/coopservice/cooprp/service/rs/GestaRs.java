package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.model.pojo.Operation;
import it.coopservice.cooprp.service.jms.MessageServiceSendToMDB;
import org.giavacms.commons.auth.jwt.annotation.AccountTokenVerification;
import org.giavacms.commons.tracer.TracerInterceptor;
import it.coopservice.cooprp.management.AppConstants;
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

@Path(AppConstants.GESTA_RP_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
@Interceptors({ TracerInterceptor.class })
public class GestaRs implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected final Logger logger = Logger.getLogger(getClass());

   @Context
   HttpServletRequest httpServletRequest;

   @Inject MessageServiceSendToMDB messageServiceSendToMDB;
   @POST
   public Response registerOperation(Operation operation)
   {
      messageServiceSendToMDB.sendMessageOperation(operation);
      return null;
   }
}
