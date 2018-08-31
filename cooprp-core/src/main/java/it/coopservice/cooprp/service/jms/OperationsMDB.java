package it.coopservice.cooprp.service.jms;

import it.coopservice.cooprp.management.AppConstants;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(name = "CodiciIPARicercaNuoviMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConstants.OPERATIONS_QUEUE),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "5"),
        @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "14400"),
        @ActivationConfigProperty(propertyName = "dLQMaxResent", propertyValue = "0")})
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors({TracerInterceptor.class})
public class OperationsMDB implements MessageListener {
    Logger logger = Logger.getLogger(getClass());

    @Override
    public void onMessage(Message message) {
        MapMessage msg = (MapMessage) message;
        String idditta = null;
        Long from = null;
        Long to = null;


    }

}
