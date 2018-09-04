package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Language;
import it.coopservice.cooprp.repository.LanguagesRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.jwt.annotation.AccountTokenVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

}

