package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Operation;
import it.coopservice.cooprp.repository.OperationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.jwtcookie.annotation.AccountCookieAndTokenVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.OPERAZIONI_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountCookieAndTokenVerification
public class OperationsRepositoryRs extends RsRepositoryService<Operation>
{

   public OperationsRepositoryRs()
   {

   }

   @Inject
   public OperationsRepositoryRs(OperationsRepository operationsRepository)
   {
      super(operationsRepository);
   }
}
