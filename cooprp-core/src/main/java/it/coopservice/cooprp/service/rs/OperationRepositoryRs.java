package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Operation;
import it.coopservice.cooprp.repository.BaseRepository;
import it.coopservice.cooprp.repository.OperationRepository;
import org.giavacms.api.repository.Search;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path(AppConstants.OPERAZIONI_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class OperationRepositoryRs extends RsRepositoryService<Operation>
{

   public OperationRepositoryRs()
   {

   }

   public OperationRepositoryRs(OperationRepository operationRepository)
   {
      super(operationRepository);
   }
}
