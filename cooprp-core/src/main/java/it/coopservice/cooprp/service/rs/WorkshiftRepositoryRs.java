package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Workshift;
import it.coopservice.cooprp.repository.BaseRepository;
import it.coopservice.cooprp.repository.WorkshiftRepository;
import org.giavacms.api.repository.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path(AppConstants.WORKSHIFTS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class WorkshiftRepositoryRs extends RsRepositoryService<Workshift>
{
   public WorkshiftRepositoryRs() {

   }

   @Inject
   public WorkshiftRepositoryRs(WorkshiftRepository workshiftRepository) {
      super(workshiftRepository);
   }
}
