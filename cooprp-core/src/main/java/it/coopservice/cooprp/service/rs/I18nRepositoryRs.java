package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Language;
import it.coopservice.cooprp.model.pojo.LanguageStatus;
import it.coopservice.cooprp.repository.LanguagesRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path(AppConstants.I18N_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class I18nRepositoryRs
{
   protected final Logger logger = Logger.getLogger(getClass());

   @Inject
   LanguagesRepository languagesRepository;

   @GET
   public Response getLanguages()
   {
      try
      {
         List<LanguageStatus> result = languagesRepository.getLangugaesStatus();
         return Response.ok(result).build();
      }
      catch (Exception ex)
      {
         logger.error(ex);
         return jsonErrorMessageResponse(ex);
      }

   }

   @GET
   @Path("/{language}")
   public Response getLanguageMap(@PathParam("language") String language)
   {
      try
      {
         JsonObject jo = languagesRepository.getLanguageMap(language);
         return Response.ok(jo.toString()).build();
      }
      catch (Exception ex)
      {
         logger.error(ex);
         return jsonErrorMessageResponse(ex);
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


   public static Response jsonErrorMessageResponse(Object error)
   {
      if (error instanceof Throwable)
      {
         Throwable t = (Throwable) error;
         return jsonResponse(Response.Status.INTERNAL_SERVER_ERROR,
                  org.giavacms.api.management.AppConstants.JSON_ERROR_KEY, t.getMessage() == null ? t.getClass()
                           .getCanonicalName() : t.getMessage());
      }
      else
      {
         return jsonResponse(Response.Status.INTERNAL_SERVER_ERROR,
                  org.giavacms.api.management.AppConstants.JSON_ERROR_KEY, "" + error);
      }
   }


   public static Response jsonResponse(Response.Status status, String key, Object value)
   {
      JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
      jsonObjBuilder.add(key, value.toString());
      JsonObject jsonObj = jsonObjBuilder.build();
      return Response.status(status).entity(jsonObj.toString()).build();
   }
}

