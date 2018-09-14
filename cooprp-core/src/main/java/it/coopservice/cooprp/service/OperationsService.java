package it.coopservice.cooprp.service;

import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.CompanyConfiguration;
import it.coopservice.cooprp.model.Location;
import it.coopservice.cooprp.model.Operation;
import it.coopservice.cooprp.repository.CompanyConfigurationsRepository;
import it.coopservice.cooprp.repository.LocationsRepository;
import it.coopservice.cooprp.repository.OperationsRepository;
import it.coopservice.cooprp.util.MailUtils;
import org.giavacms.api.management.AppConstants;
import org.giavacms.core.util.RestStaticClient;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ObjectMessage;
import java.util.Collections;
import java.util.HashMap;

@Stateless
public class OperationsService
{
   @Inject CompanyConfigurationsRepository companyConfigurationsRepository;

   @Inject LocationsRepository locationsRepository;

   @Inject OperationsRepository operationsRepository;

   protected final Logger logger = Logger.getLogger(getClass().getName());

   public void sendToWsOperazioni(Operation operation)
   {

      try
      {
         CompanyConfiguration companyConfiguration = companyConfigurationsRepository
                  .find(operation.companyConfiguration_uuid);

         //TODO 1) ricerca della location (per società) che si trova nell'intorno specificato dalla configurazione
         Location location = locationsRepository.findLocation(operation.latitudine, operation.longitudine);
         logger.info(" FOUND LOCATION: " + location.nome + " FOR OPERATION: " + operation.uuid);

         if (location != null && companyConfiguration.gestisciPrivacy)
         {
            operationsRepository.updateLocationAndDeleteCoordinates(operation.uuid, location.uuid);
         }
         else if (location != null)
         {
            operationsRepository.updateLocation(operation.uuid, location.uuid);
         }

         if (location != null || companyConfiguration.forzaScrittura)
         {
            //TODO 2) se trovata: se previsto da configurazione, invio dell'operazione di IN o OUT all'applicazione corrispondente
            //TODO 3) se non trovata: se previsto da configurazione (anche in caso di mancata localizzazione), invio dell'operazione di IN o OUT all'applicazione corrispondente
            if (companyConfiguration.wsOperazioni != null && !companyConfiguration.wsOperazioni.trim().isEmpty())
            {
               RestStaticClient.post(AppConstants.JBOSS_QUALIFIED_HOST_NAME_PROPERTY,
                        companyConfiguration.wsOperazioni,
                        operation,
                        Operation.class,
                        new HashMap<>(),
                        new HashMap<>(),
                        new HashMap<>());
            }
         }

         //TODO 2bis)se previsto da configurazione, eliminazione delle informazioni di localizzazione (lat e lon)

         //se previsto da configurazione, invio email con segnalazione contenente le informazioni su dipendente
         if (location == null && companyConfiguration.mail != null && !companyConfiguration.mail.trim().isEmpty())
         {
            MailUtils.send("Empty Location", operation.toString(),
                     Collections.singleton(companyConfiguration.mail.trim()),
                     Collections.singleton(AppProperties.sawMail.value().trim()));
         }

         logger.info("SENDING OPERATION UUID : " + operation.uuid + "TO WS_OPERAZIONI: "
                  + companyConfiguration.wsOperazioni);

      }
      catch (Exception e)
      {
         logger.error(e);
         e.printStackTrace();
      }
   }
}
