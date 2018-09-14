package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.CompanyConfiguration;
import it.coopservice.cooprp.model.Operation;
import it.coopservice.cooprp.repository.CompanyConfigurationsRepository;
import it.coopservice.cooprp.repository.OperationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.jwtcookie.annotation.AccountCookieAndTokenVerification;
import org.giavacms.commons.util.DateUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

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

   @Inject CompanyConfigurationsRepository companyConfigurationsRepository;

   @Override protected void prePersist(Operation object) throws Exception
   {
      convertDataOra(object);
      convertSocietaToCompanyConfiguration(object);
      super.prePersist(object);
   }

   @Override protected Operation preUpdate(Operation object) throws Exception
   {
      convertDataOra(object);
      convertSocietaToCompanyConfiguration(object);
      return super.preUpdate(object);
   }

   private void convertDataOra(Operation object) throws Exception
   {
      Date realDate = null;
      SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.ISO_FORMATTER);
      if (object.dataOra != null && object.timezone != null && !object.timezone.trim().isEmpty())
      {
         realDate = DateUtils.getDateFromStringAndTimezone(object.dataOra, object.timezone);
         logger.warn("converted: " + object.dataOra + ", TZ: " + object.timezone + " to timezone: " + sdf
                  .format(realDate));
      }
      else if (object.dataOra != null)
      {
         realDate = DateUtils.getDateFromStringAndTimezone(object.dataOra, AppConstants.DEFAULT_TIMEZONE);
         logger.warn("converted: " + object.dataOra + ", TZ: " + object.timezone + " to timezone: " + sdf
                  .format(realDate));
      }

      object.realDate = realDate;
   }

   private void convertSocietaToCompanyConfiguration(Operation object) throws Exception
   {

      if (object.societaId == null)
      {
         throw new Exception("Occorre specificare la societa'");
      }
      object.companyConfiguration_uuid = companyConfigurationsRepository.findBySocietaId(object.societaId);
   }
}
