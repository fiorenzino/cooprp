package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Privacy;
import it.coopservice.cooprp.repository.PrivaciesRepository;
import org.giavacms.api.repository.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.jwtcookie.annotation.AccountCookieAndTokenVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(AppConstants.PRIVACY_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountCookieAndTokenVerification
public class PrivaciesRepositoryRs extends RsRepositoryService<Privacy>
{
   @Inject
   PrivaciesRepository privaciesRepository;

   public PrivaciesRepositoryRs()
   {
   }

   @Inject
   public PrivaciesRepositoryRs(PrivaciesRepository repository)
   {
      super(repository);
   }

   @Path("/users/{codiceFiscale}")
   @GET
   public Response fetchByCodiceFiscale(@PathParam("codiceFiscale") String codiceFiscale)
   {
      try
      {
         Search<Privacy> search = new Search<>(Privacy.class);
         search.getObj().codiceFiscale = codiceFiscale;
         List<Privacy> list = privaciesRepository.getList(search, 0, 1);
         Privacy privacy = list.size() > 0 ? list.get(0) : null;
         return Response.ok().entity(privacy).build();
      } catch (Exception ex)
      {
         ex.printStackTrace();
         return jsonErrorMessageResponse(ex);
      }
   }

   @Path("/users/{codiceFiscale}/accept")
   @GET
   public Response acceptPrivacy(@PathParam("codiceFiscale") String codiceFiscale)
   {
      try
      {
         boolean returned = privaciesRepository.updateAccepted(codiceFiscale);
         return Response.ok().build();
      } catch (Exception ex)
      {
         ex.printStackTrace();
         return jsonErrorMessageResponse(ex);
      }
   }
}
