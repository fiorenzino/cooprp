package org.giavacms.core.util;

import org.apache.commons.io.IOUtils;
import org.giavacms.commons.auth.jwt.model.pojo.JWTLogin;
import org.giavacms.commons.auth.jwt.model.pojo.JWTToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fiorenzo on 06/06/17.
 */
public class RestStaticClient {

    static Logger logger = Logger.getLogger(org.giavacms.core.util.RestStaticClient.class);


    public static String login(String targetHost, String username, String password) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost)) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            JWTLogin jwtLogin = new JWTLogin();
            webTargetClosable.response = webTargetClosable.webTarget
                    .request()
                    .buildPost(Entity.json(jwtLogin)).invoke();
            JWTToken jwtToken = webTargetClosable.response.readEntity(JWTToken.class);
            logger.info("token: " + jwtToken.getToken());
            return jwtToken.getToken();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static <T> List<T> list(String targetHost, String targetPath,
                                   GenericType<List<T>> genericType,
                                   Map<String, Object> queryParams,
                                   Map<String, Object> pathParams,
                                   Map<String, Object> headerParams) throws Exception {
        List<T> wrapper = null;
        System.out.println("Invoco:" + targetHost + targetPath);
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    invocationBuilder = invocationBuilder.header(key, headerParams.get(key));
                }
            }
            wrapper = invocationBuilder.get(genericType);


            if (wrapper != null) {
//                for (Object entity : wrapper) {
//                    logger.info(entity);
//                }
                logger.info("getListSize: " + wrapper.size());
            }
            return wrapper;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static String listSize(String targetHost, String targetPath,
                                  Map<String, Object> queryParams,
                                  Map<String, Object> pathParams,
                                  Map<String, Object> headerParams) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            webTarget = webTarget.queryParam("startRow", 0);
            webTarget = webTarget.queryParam("", 1);
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    invocationBuilder = invocationBuilder.header(key, headerParams.get(key));
                }
            }
            String listSize = invocationBuilder.buildGet().invoke().getHeaderString("listSize");

            logger.info("getListSize: " + listSize);

            return listSize;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static <T> T get(String targetHost, String targetPath,
                            Class<T> t, Map<String, Object> queryParams,
                            Map<String, Object> pathParams,
                            Map<String, Object> headerParams) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            Invocation.Builder builder = webTarget.request();
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    builder = builder.header(key, headerParams.get(key));
                }
            }
            webTargetClosable.response = builder.buildGet().invoke();


            if (webTargetClosable.response.getStatus() == Response.Status.OK.getStatusCode()) {
                T result = webTargetClosable.response.readEntity(t);
                logger.info("result: " + result);
                return result;
            } else if (webTargetClosable.response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ||
                    webTargetClosable.response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
                logger.info("No content");
                return null;
            } else {
                String message = "[" + webTargetClosable.response.getStatus() + "] - " + webTargetClosable.response.readEntity(String.class);
                throw new RuntimeException(message);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static <T> T post(String targetHost, String targetPath,
                             Object entity,
                             Class<T> t, Map<String, Object> queryParams,
                             Map<String, Object> pathParams,
                             Map<String, Object> headerParams) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            Invocation.Builder builder = webTarget.request();
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    builder = builder.header(key, headerParams.get(key));
                }
            }
            webTargetClosable.response = builder.buildPost(Entity.json(entity)).invoke();
            if (webTargetClosable.response.getStatus() == Response.Status.OK.getStatusCode()) {
                T result = webTargetClosable.response.readEntity(t);
                logger.info("result: " + result);
                return result;
            } else if (webTargetClosable.response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ||
                    webTargetClosable.response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
                logger.info("No content");
                return null;
            } else {
                String message = "[" + webTargetClosable.response.getStatus() + "] - " + webTargetClosable.response.readEntity(String.class);
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static <T> T put(String targetHost, String targetPath,
                            Object entity,
                            Class<T> t, Map<String, Object> queryParams,
                            Map<String, Object> pathParams,
                            Map<String, Object> headerParams) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (pathParams != null && !pathParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : pathParams.entrySet()) {
                    webTarget = webTarget.resolveTemplate(coppia.getKey(), coppia.getValue());
                }
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            Invocation.Builder builder = webTarget.request();
            if (headerParams != null && !headerParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : headerParams.entrySet()) {
                    builder = builder.header(coppia.getKey(), coppia.getValue());
                }
            }
            webTargetClosable.response = builder.buildPut(Entity.json(entity)).invoke();
            if (webTargetClosable.response.getStatus() == Response.Status.OK.getStatusCode()) {
                T result = webTargetClosable.response.readEntity(t);
                logger.info("result: " + result);
                return result;
            } else if (webTargetClosable.response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ||
                    webTargetClosable.response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {
                return null;
            } else {
                String message = "[" + webTargetClosable.response.getStatus() + "] - " + webTargetClosable.response.readEntity(String.class);
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static void delete(String targetHost, String targetPath,
                              Map<String, String> queryParams,
                              Map<String, String> pathParams,
                              Map<String, Object> headerParams) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, String> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            Invocation.Builder invocationBuilder =
                    webTarget.request(MediaType.APPLICATION_JSON);
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    invocationBuilder = invocationBuilder.header(key, headerParams.get(key));
                }
            }
            Response result = invocationBuilder.delete();
            logger.info(result.getStatus());

            if (Response.Status.NO_CONTENT.getStatusCode() != result.getStatus() &&
                    Response.Status.OK.getStatusCode() != result.getStatus()) {
                throw new RuntimeException(result.readEntity(String.class));
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public static byte[] download(String targetHost, String targetPath,
                                  Map<String, Object> queryParams,
                                  Map<String, Object> pathParams,
                                  Map<String, Object> headerParams) throws Exception {
        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }

            Invocation.Builder builder = webTarget.request();
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    builder = builder.header(key, headerParams.get(key));
                }
            }

            webTargetClosable.response = builder.buildGet().invoke();

            if (webTargetClosable.response.getStatus() != Response.Status.OK.getStatusCode() &&
                    webTargetClosable.response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
                String message = "[" + webTargetClosable.response.getStatus() + "] - " + webTargetClosable.response.readEntity(String.class);
                throw new RuntimeException(message);
            }

            InputStream result = webTargetClosable.response.readEntity(InputStream.class);
            byte[] stream = IOUtils.toByteArray(result);
            return stream;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }


    public static <T> T upload(String method, String targetHost, String targetPath,
                               Map<String, Object> multipartParams,
                               Map<String, Object> queryParams,
                               Map<String, Object> pathParams,
                               Map<String, Object> headerParams, Class<T> t) throws Exception {


        int streamCount = 0;

        try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
            WebTarget webTarget = webTargetClosable.webTarget;
            if (pathParams != null && !pathParams.isEmpty()) {
                for (String key : pathParams.keySet()) {
                    webTarget = webTarget.resolveTemplate(key, pathParams.get(key));
                }
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> coppia : queryParams.entrySet()) {
                    webTarget = webTarget.queryParam(coppia.getKey(), coppia.getValue());
                }
            }
            Invocation.Builder builder = webTarget.request();
            if (headerParams != null && !headerParams.isEmpty()) {
                for (String key : headerParams.keySet()) {
                    builder = builder.header(key, headerParams.get(key));
                }
            }

            if (headerParams == null) {
                headerParams = new HashMap<>();
            }

            headerParams.put(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA);


            MultipartFormDataOutput multipartFormDataOutput = new MultipartFormDataOutput();

            for (String key : multipartParams.keySet()) {
                if (multipartParams.get(key) instanceof File) {
                    String fileKey = streamCount == 0 ? "file" : "file" + (++streamCount);
                    File file = (File) multipartParams.get(key);
                    multipartFormDataOutput.addFormData(fileKey, IOUtils.toByteArray(file.toURI()), MediaType.APPLICATION_OCTET_STREAM_TYPE, key);
                } else if (multipartParams.get(key) instanceof InputStream) {
                    String fileKey = streamCount == 0 ? "file" : "file" + (++streamCount);
                    multipartFormDataOutput.addFormData(fileKey, (InputStream) multipartParams.get(key), MediaType.APPLICATION_OCTET_STREAM_TYPE, key);
                } else if (multipartParams.get(key) instanceof byte[]) {
                    String fileKey = streamCount == 0 ? "file" : "file" + (++streamCount);
                    multipartFormDataOutput.addFormData(fileKey, (byte[]) multipartParams.get(key), MediaType.APPLICATION_OCTET_STREAM_TYPE, key);
                } else {
                    multipartFormDataOutput.addFormData(key, multipartParams.get(key), MediaType.TEXT_PLAIN_TYPE);
                }
            }

            GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipartFormDataOutput) {
            };

            if (method.equalsIgnoreCase(HttpMethod.POST)) {
                webTargetClosable.response = builder.buildPost(Entity.entity(multipartFormDataOutput, MediaType.MULTIPART_FORM_DATA_TYPE)).invoke();
            } else if (method.equalsIgnoreCase(HttpMethod.PUT)) {
                webTargetClosable.response = builder.buildPut(Entity.entity(multipartFormDataOutput, MediaType.MULTIPART_FORM_DATA_TYPE)).invoke();
            } else {
                throw new UnsupportedOperationException("HTTP Method not supported!");
            }

            if (webTargetClosable.response.getStatus() != Response.Status.OK.getStatusCode() &&
                    webTargetClosable.response.getStatus() != Response.Status.ACCEPTED.getStatusCode()) {
                String message = "[" + webTargetClosable.response.getStatus() + "] - " + webTargetClosable.response.readEntity(String.class);
                throw new RuntimeException(message);
            } else if (webTargetClosable.response.getStatus() == Response.Status.OK.getStatusCode()) {
                T result = webTargetClosable.response.readEntity(t);
                logger.info("result: " + result);
                return result;
            } else return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

}
