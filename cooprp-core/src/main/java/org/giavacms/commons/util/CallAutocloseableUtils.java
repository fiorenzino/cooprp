package org.giavacms.commons.util;

import org.jboss.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

public class CallAutocloseableUtils {

   static Logger logger = Logger.getLogger(CallAutocloseableUtils.class);

   public static boolean call(String targetHost, String targetPath, Object object) throws Exception {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath)) {
         webTargetClosable.response = webTargetClosable.webTarget
                 .request()
                 .buildPost(
                         Entity.entity(object, MediaType.APPLICATION_JSON))
                 .invoke();
         if (webTargetClosable.response != null
                 && (webTargetClosable.response.getStatus() == Status.OK.getStatusCode()
                 || webTargetClosable.response.getStatus() == Status.NO_CONTENT.getStatusCode())) {
            return true;
         } else {
            logger.error("targetHost:" + targetHost + ",targetPath: " + targetPath + "code: "
                    + webTargetClosable.response.getStatus() + " - status: "
                    + Status.fromStatusCode(webTargetClosable.response.getStatus())
                    + " - family: "
                    + Status.Family.familyOf(webTargetClosable.response.getStatus()));
         }
      } catch (Exception e) {
         logger.error(e.getMessage(), e);
         return false;
      }
      return false;
   }

   public static void noResponse(String targetHost, Object object) throws Exception {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost)) {
         webTargetClosable.response = webTargetClosable.webTarget
                 .request()
                 .buildPost(
                         Entity.entity(object, MediaType.APPLICATION_JSON))
                 .invoke();
      } catch (Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   public static void reset(String targetHost) throws Exception {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost)) {
         webTargetClosable.response = webTargetClosable.webTarget
                 .request().buildGet().invoke();
      } catch (Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   public static String workload(String targetHost) throws Exception {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost)) {
         webTargetClosable.response = webTargetClosable.webTarget
                 .request().buildGet().invoke();
         String workload = webTargetClosable.response.readEntity(String.class);
         logger.info("workload: " + workload);
         return workload;
      } catch (Exception e) {
         logger.error(e.getMessage(), e);
         return "";
      }
   }

   public static String best(String targetHost, String uuid) {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost)) {
         webTargetClosable.response = webTargetClosable.webTarget.request().buildGet().invoke();

         String best = webTargetClosable.response.readEntity(String.class);

         if (webTargetClosable.response.getStatus() != Status.OK.getStatusCode()) {
            throw new Exception("Error invoking " + targetHost + " - [ " + best + " ]");
         }

         logger.info("[" + uuid + "]  - best: " + best);
         if (best.contains("RS_MSG")) {
            return null;
         }
         return best;
      } catch (Exception e) {
         logger.error(e.getMessage(), e);
      }
      return null;
   }

}
