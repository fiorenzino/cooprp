package it.coopservice.cooprp.service.jms;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Operation;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
@AccessTimeout(unit = TimeUnit.MINUTES, value = 60L)
@Interceptors({TracerInterceptor.class})
public class MessageServiceSendToMDB {

    Logger logger = Logger.getLogger(getClass());

    Context context = null;
    ConnectionFactory connectionFactory = null;
    Connection connection = null;
    Session session = null;
    Map<String, MessageProducer> producers = null;

    @PostConstruct
    public void postConstruct() {
        try {
            producers = new HashMap<String, MessageProducer>();
            initialize();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t);
        }
    }

    private void initialize() throws Exception {
        logger.info("Initializing...");
        context = new InitialContext();
        connectionFactory = (ConnectionFactory) context
                .lookup("ConnectionFactory");
        if (connection != null) {
            try {
                connection.close();
            } catch (Throwable e) {
            }
        }
        connection = connectionFactory.createConnection();
        if (session != null) {
            try {
                session.close();
            } catch (Throwable e) {
            }
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        for (String existingQueueName : producers.keySet()) {
            MessageProducer existingProducer = producers.get(existingQueueName);
            try {
                existingProducer.close();
            } catch (Throwable ex) {
            }
            Queue existingQueue = (Queue) context.lookup(existingQueueName);
            producers.put(existingQueueName, session.createProducer(existingQueue));
        }
        connection.start();
    }

    private void addProducer(String queueName) throws Exception {
        Queue queue = null;
        try {
            queue = (Queue) context.lookup(queueName);
        } catch (Throwable e) {
            throw new Exception("ERRORE DI LOOKUP per " + queueName + " : " + e.getClass().getCanonicalName() + ", msg: "
                    + e.getMessage());
        }
        if (queue == null) {
            throw new Exception("CODA " + queueName + " NON CONFIGURATA!!");
        }

        try {
            producers.put(queueName, session.createProducer(queue));
        } catch (Throwable e) {
            logger.warn("Errore nella creazione di un messageProducer per " + queueName + " --> reinizializzo.");
            initialize();
            producers.put(queueName, session.createProducer(queue));
        }
    }

    private void send(Message msg, String queueName) throws Exception {
        MessageProducer producer = producers.get(queueName);
        if (producer == null) {
            addProducer(queueName);
            producer = producers.get(queueName);
        }

        try {
            producer.send(msg);
        } catch (Throwable e) {
            logger.warn("Errore nell'inivio tramite un messageProducer esistente per " + queueName + " --> reinizializzo.");
            initialize();
            producer = producers.get(queueName);
            producer.send(msg);
        }

    }

    @PreDestroy
    public void preDestroy() {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendMessageOperation(Operation operation) {
        MapMessage msg = null;
        try {
            msg = session.createMapMessage();
        } catch (Throwable t) {
        }
        try {
            if (msg == null) {
                initialize();
                msg = session.createMapMessage();
            }


            send(msg, AppConstants.OPERATIONS_QUEUE);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }

}
