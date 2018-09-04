package org.giavacms.api.util;

import org.giavacms.api.model.HeapStatus;
import org.giavacms.api.model.NonHeapStatus;
import org.giavacms.api.model.QueueStatus;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import javax.security.auth.callback.*;
import javax.security.sasl.RealmCallback;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class CliUtils {

    public static final String UNDEFINED = "undefined";
    public static final String READ_RESOURCE = ClientConstants.READ_RESOURCE_OPERATION;
    public static final String READ_ATTRIBUTE = ClientConstants.READ_ATTRIBUTE_OPERATION;
    private static final String SYSTEM_PROPERTY = "system-property";
    public static final String _Q = "queue/";

    private static final String SUCCESS = "success";
    private static final String RESULT = "result";
    private static final String OUTCOME = "outcome";
    private static final String VALUE = "value";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";

    public static ModelControllerClient connect(int port) {
        return connect("localhost", port, null, null, null);
    }

    public static ModelControllerClient connect(String host, int port,
                                                String username, String password, String realmName) {
        try {
            if (username == null) {
                return ModelControllerClient.Factory.create(
                        InetAddress.getByName(host), port);
            } else {
                return createClient(InetAddress.getByName(host), port,
                        username, password, realmName);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private static ModelControllerClient createClient(final InetAddress host,
                                                      final int port, final String username, final String password,
                                                      final String securityRealmName) {

        final CallbackHandler callbackHandler = new CallbackHandler() {

            public void handle(Callback[] callbacks) throws IOException,
                    UnsupportedCallbackException {
                for (Callback current : callbacks) {
                    if (current instanceof NameCallback) {
                        NameCallback ncb = (NameCallback) current;
                        ncb.setName(username);
                    } else if (current instanceof PasswordCallback) {
                        PasswordCallback pcb = (PasswordCallback) current;
                        pcb.setPassword(password.toCharArray());
                    } else if (current instanceof RealmCallback) {
                        RealmCallback rcb = (RealmCallback) current;
                        rcb.setText(rcb.getDefaultText());
                    } else {
                        throw new UnsupportedCallbackException(current);
                    }
                }
            }
        };

        return ModelControllerClient.Factory
                .create(host, port, callbackHandler);
    }

    /**
     * @param client
     * @param attribute
     * @return null in caso di errori catchati o di errori delle funzioni che
     * chiama a sua volta
     */
    public static String read(ModelControllerClient client,
                              String operationType, String resourceAddr, String attribute,
                              Boolean recursive, Boolean includeRuntime) {

        if (operationType == null) {
            operationType = ClientConstants.READ_RESOURCE_OPERATION;
        }
        try {
            ModelNode request = new ModelNode();
            request.get(ClientConstants.OP).set(operationType);
            if (resourceAddr == null) {
                request.get(ClientConstants.OP_ADDR).add(
                        CliUtils.SYSTEM_PROPERTY, attribute);
            } else {
                if (resourceAddr.startsWith("/")) {
                    resourceAddr = resourceAddr.substring(1);
                }
                for (String path : resourceAddr.split("/")) {
                    request.get(ClientConstants.OP_ADDR).add(
                            path.split("=")[0], path.split("=")[1]);
                }
                if (attribute != null) {
                    request.get(ClientConstants.NAME).set(attribute);
                }
            }
            if (recursive != null) {
                request.get(ClientConstants.RECURSIVE).set(true);
            }
            if (includeRuntime != null) {
                request.get(ClientConstants.INCLUDE_RUNTIME)
                        .set(includeRuntime);
            }
            ModelNode returnVal = client.execute(request);

            return returnVal.get(ClientConstants.RESULT).toJSONString(false);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static QueueStatus readQueue(ModelControllerClient client,
                                        String queueName) {

        try {
            if (queueName.startsWith(_Q)) {
                queueName = queueName.replace(_Q, "");
            }
            String operationType = CliUtils.READ_RESOURCE;
            String resourceAddr = "/subsystem=messaging-activemq/server=default/jms-queue="
                    + queueName;
            String attribute = null;
            Boolean recursive = true;
            Boolean includeRuntime = true;
            String result = CliUtils.read(client, operationType, resourceAddr,
                    attribute, recursive, includeRuntime);
            if (result == null) {
                System.out.println("READ ERROR");
                return null;
            }
            return new QueueStatus(result);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static HeapStatus readHeap(ModelControllerClient client) {

        try {

            String operationType = CliUtils.READ_ATTRIBUTE;
            String resourceAddr = "/core-service=platform-mbean/type=memory";
            String attribute = "heap-memory-usage";
            Boolean recursive = true;
            Boolean includeRuntime = true;
            String json = CliUtils.read(client, operationType, resourceAddr,
                    attribute, recursive, includeRuntime);
            if (json == null) {
                System.out.println("READ ERROR");
                return null;
            }
            return new HeapStatus(json);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static NonHeapStatus readNonHeap(ModelControllerClient client) {

        try {

            String operationType = CliUtils.READ_ATTRIBUTE;
            String resourceAddr = "/core-service=platform-mbean/type=memory";
            String attribute = "non-heap-memory-usage";
            Boolean recursive = true;
            Boolean includeRuntime = true;
            String json = CliUtils.read(client, operationType, resourceAddr,
                    attribute, recursive, includeRuntime);
            if (json == null) {
                System.out.println("READ ERROR");
                return null;
            }
            return new NonHeapStatus(json);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    /**
     * @param client
     * @param name
     * @param expectedValue
     * @return null in caso di errori catchati o di errori delle funzioni che chiama a sua volta
     */
    public static Boolean checkOnJBoss(ModelControllerClient client, String name, String expectedValue) {
        try {
            String actualValue = readOnJBoss(client, name);
            if (actualValue == null) {
                // ERRORE DI LETTURA NON PROSEGUO E PROPAGO ERRORE
                return null;
            }
            if (UNDEFINED.equals(actualValue)) {
                return false;
            } else {
                return expectedValue.equals(actualValue);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    /**
     * @param client
     * @param name
     * @return null in caso di errori catchati o di errori delle funzioni che chiama a sua volta
     */
    public static String readOnJBoss(ModelControllerClient client, String name) {
        try {
            ModelNode request = new ModelNode();
            request.get(ClientConstants.OP).set(READ_RESOURCE);
            request.get(ClientConstants.OP_ADDR).add(SYSTEM_PROPERTY, name);
            ModelNode returnVal = client.execute(request);
            // if (!SUCCESS.equals(returnVal.get(OUTCOME).asString()))
            // {
            // return UNDEFINED;
            // }
            return returnVal.get(RESULT).get(VALUE).asString();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    /**
     * @param client
     * @param name
     * @param newValue
     * @return null in caso di errore
     */
    public static Boolean writeOnJBoss(ModelControllerClient client, String name, String newValue) {
        try {
            // verifico se presente
            String currentValue = readOnJBoss(client, name);
            if (currentValue == null) {
                // ERRORE DI LETTURA NON PROSEGUO E PROPAGO ERRORE
                return null;
            }
            if (!UNDEFINED.equals(currentValue)) {
                // VALORE ESISTENTE
                if (currentValue.equals(newValue)) {
                    // GIA' A POSTO COSI
                    return true;
                } else {
                    // DEVO RIMUOVERE IL VECCHIO VALORE
                    Boolean removeOnJboss = removeOnJBoss(client, name);
                    if (removeOnJboss == null) {
                        // ERRORE DI RIMOZIONE NON PROSEGUO E PROPAGO ERRORE
                        return null;
                    }
                    if (!removeOnJboss.booleanValue()) {
                        // FALLITA RIMOZIONE NON PROSEGUO E RITORNO FALSE
                        return false;
                    }
                }
            }

            // SCRITTURA DEL NUOVO VALORE
            ModelNode writeRequest = new ModelNode();
            writeRequest.get(ClientConstants.OP).set(ADD);
            writeRequest.get(ClientConstants.OP_ADDR).add(SYSTEM_PROPERTY, name);
            writeRequest.get(VALUE).set(newValue);
            ModelNode returnVal = client.execute(new OperationBuilder(writeRequest).build());
            if (!SUCCESS.equals(returnVal.get(OUTCOME).asString())) {
                return false;
            }
            String newCurrentValue = readOnJBoss(client, name);
            if (newCurrentValue == null) {
                // ERRORE DI RILETTURA NON PROSEGUO E PROPAGO ERRORE
                return null;
            }
            return newCurrentValue.equals(newValue);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static Boolean removeOnJBoss(ModelControllerClient client, String name) {
        try {
            ModelNode request = new ModelNode();
            request.get(ClientConstants.OP).set(REMOVE);
            request.get(ClientConstants.OP_ADDR).add(SYSTEM_PROPERTY, name);
            ModelNode returnVal = client.execute(request);
            if (!SUCCESS.equals(returnVal.get(OUTCOME).asString())) {
                return false;
            }
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static List<String> listProperties(ModelControllerClient client, String prefix) {
        try {
            ModelNode request = new ModelNode();
            request.get(ClientConstants.OP).set(READ_RESOURCE);
            request.get(ClientConstants.OP_ADDR).add(SYSTEM_PROPERTY);
            ModelNode returnVal = client.execute(request);
            if (!SUCCESS.equals(returnVal.get(OUTCOME).asString())) {
                return null;
            }
            List<String> prefixedProperties = new ArrayList<String>();
            for (Property p : returnVal.get(RESULT).get(SYSTEM_PROPERTY).asPropertyList()) {
                if (p.getName().startsWith(prefix)) {
                    prefixedProperties.add(p.getName());
                }
            }
            return prefixedProperties;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

}
