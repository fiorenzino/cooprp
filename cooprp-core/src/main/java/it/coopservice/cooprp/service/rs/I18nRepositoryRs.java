package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.pojo.LanguageStatus;
import it.coopservice.cooprp.repository.LanguagesRepository;
import org.giavacms.api.service.RsResponseService;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(AppConstants.I18N_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class I18nRepositoryRs extends RsResponseService
{
   protected final Logger logger = Logger.getLogger(getClass());

   @Inject
   LanguagesRepository languagesRepository;

   public I18nRepositoryRs () {

   }

   @GET
   @Path("/")
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
   @Path("/{lang}")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response getLanguageMap(@PathParam("lang") String lang)
   {
      try
      {
         JsonObject jo = languagesRepository.getLanguageMap(lang);
         return Response.ok(jo.toString()).build();
      }
      catch (Exception ex)
      {
         logger.error(ex);
         return jsonErrorMessageResponse(ex);
      }
   }

}

