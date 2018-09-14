package it.coopservice.cooprp.service.jms;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Operation;
import it.coopservice.cooprp.service.OperationsService;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jms.*;

@MessageDriven(name = "OperationsMDB", activationConfig = {
         @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
         @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConstants.OPERATIONS_QUEUE),
         @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
         @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "5"),
         @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "14400"),
         @ActivationConfigProperty(propertyName = "dLQMaxResent", propertyValue = "0") })
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors({ TracerInterceptor.class })
public class OperationsMDB implements MessageListener
{
   Logger logger = Logger.getLogger(getClass());

   @Inject OperationsService operationsService;

   @Override
   public void onMessage(Message message)
   {
      try
      {
         ObjectMessage msg = (ObjectMessage) message;
         String idditta = null;
         Long from = null;
         Long to = null;

         Operation operation = null;
         operation = (Operation) msg.getObject();
         operationsService.sendToWsOperazioni(operation);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }



   }

}
