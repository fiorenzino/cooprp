package org.giavacms.core.util;

import org.jboss.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import java.util.Map;

public class CallAutocloseableUtils
{
   static Logger logger = Logger.getLogger(CallAutocloseableUtils.class);

   public static boolean call(String targetHost, String targetPath, Object content) throws Exception
   {
      try (WebTargetClosable webTargetClosable = new WebTargetClosable(targetHost, targetPath))
      {
         webTargetClosable.response = webTargetClosable.webTarget
                  .request()
                  .buildPost(
                           Entity.entity(content, MediaType.APPLICATION_JSON))

                  .invoke();
         if (webTargetClosable.response != null
                  && (
                  webTargetClosable.response.getStatus() == Status.OK.getStatusCode() ||
                           webTargetClosable.response.getStatus() == Status.NO_CONTENT.getStatusCode()))
         {
            return true;
         }
         else
         {
            logger.error("targetHost:" + targetHost + ",targetPath: " + targetPath + "code: "
                     + webTargetClosable.response.getStatus() + " - status: "
                     + Status.fromStatusCode(webTargetClosable.response.getStatus())
                     + " - family: "
                     + Status.Family.familyOf(webTargetClosable.response.getStatus()));
         }
      }
      catch (Exception e)
      {
         logger.error(e.getMessage(), e);
         return false;
      }
      return false;
   }
}