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
         String wsOperazioni = companyConfiguration.wsOperazioni;

         if (location != null && companyConfiguration.gestisciPrivacy)
         {
            logger.info(" FOUND LOCATION WITH PRIVACY CONFIGURATION: " + location.nome + " FOR OPERATION: "
                     + operation.uuid);
            //se previsto da configurazione, eliminazione delle informazioni di localizzazione (lat e lon)
            operationsRepository.updateLocationAndDeleteCoordinates(operation.uuid, location.uuid);
         }
         if (location != null && !companyConfiguration.gestisciPrivacy)
         {
            logger.info(" FOUND LOCATION WITHOUT PRIVACY CONFIGURATION: " + location.nome + " FOR OPERATION: "
                     + operation.uuid);
            operationsRepository.updateLocation(operation.uuid, location.uuid);
         }

         if (location == null && companyConfiguration.gestisciPrivacy)
         {
            //COSA FACCIO SE NON TROVO LOCATION E GESTISCO LA PRIVACY?
            operationsRepository.deleteCoordinates(operation.uuid);
         }

         //se trovata: se previsto da configurazione, invio dell'operazione di IN o OUT all'applicazione corrispondente
         //se non trovata: se previsto da configurazione (anche in caso di mancata localizzazione), invio dell'operazione di IN o OUT all'applicazione corrispondente
         if ((wsOperazioni != null && !wsOperazioni.trim().isEmpty())
                  && (location != null || companyConfiguration.forzaScrittura))
         {

            RestStaticClient.post(AppConstants.JBOSS_QUALIFIED_HOST_NAME_PROPERTY,
                     companyConfiguration.wsOperazioni,
                     operation,
                     Operation.class,
                     new HashMap<>(),
                     new HashMap<>(),
                     new HashMap<>());
         }

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
