package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Language;
import it.coopservice.cooprp.model.Location;
import it.coopservice.cooprp.model.pojo.LanguageStatus;
import it.coopservice.cooprp.repository.LanguagesRepository;
import it.coopservice.cooprp.repository.LocationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(AppConstants.LANGUAGES_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class LanguagesRepositoryRs extends RsRepositoryService<Language>
{

   @Inject
   LanguagesRepository languagesRepository;

   public LanguagesRepositoryRs()
   {

   }

   @Inject
   public LanguagesRepositoryRs(LanguagesRepository languagesRepository)
   {
      super(languagesRepository);
   }

   @GET
   @Path("/languages")
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
   @Path("/languages/{language}")
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
}

