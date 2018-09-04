package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Operation;
import it.coopservice.cooprp.repository.OperationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.jwt.annotation.AccountTokenVerification;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.OPERAZIONI_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountTokenVerification
public class OperationsRepositoryRs extends RsRepositoryService<Operation>
{

   public OperationsRepositoryRs()
   {

   }

   public OperationsRepositoryRs(OperationsRepository operationsRepository)
   {
      super(operationsRepository);
   }
}
