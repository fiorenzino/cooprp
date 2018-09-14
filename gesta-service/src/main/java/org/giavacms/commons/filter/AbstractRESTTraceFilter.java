package org.giavacms.commons.filter;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Provider
//@PreMatching
public abstract class AbstractRESTTraceFilter implements
         ContainerResponseFilter,
         ContainerRequestFilter
{
   private final Logger logger = Logger.getLogger(getClass());

   public void filter(ContainerRequestContext requestContext) throws IOException
   {
      Map<String, Object> traccia = new HashMap<>();
      traccia.put("dataora", new Date());
      StringBuffer sb = new StringBuffer();
      try
      {
         traccia.put("utente", requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown"
                  : requestContext.getSecurityContext().getUserPrincipal().getName());
         sb.append("User: ").append(traccia.get("utente"));

         String path = requestContext.getUriInfo().getPath();
         String[] split = requestContext.getUriInfo().getPath().split("/");
         if (split.length >= 3)
         {
            traccia.put("servizio", split[2]);
            traccia.put("operazione", path.substring(1 + split[1].length() + 1 + split[2].length()));
         }
         else
         {
            traccia.put("servizio", requestContext.getUriInfo().getPath());
            traccia.put("operazione", "");
         }
         sb.append(" - Path: ").append(path);

         traccia.put("agente", requestContext.getHeaderString("User-Agent"));
         sb.append(" - Headers: ").append(requestContext.getHeaders());

         traccia.put("metodo", requestContext.getMethod());
         sb.append(" - Method: ").append(traccia.get("metodo"));

         // traccia.put("stato", responseCtx.getStatus());
         if ("PUT".equals(requestContext.getMethod()) || "POST".equals(requestContext.getMethod()))
         {
            try
            {
               if (MediaType.APPLICATION_FORM_URLENCODED_TYPE.equals(requestContext.getMediaType()))
               {

                  traccia.put("contenuto",
                           ((org.jboss.resteasy.specimpl.RequestImpl) requestContext.getRequest())
                                    .getFormParameters() + ""
                  );
                  sb.append(" - Params: ").append(traccia.get("contenuto"));
               }
               else
               {
                  String entityBody = getEntityBody(requestContext).trim();
                  if (entityBody.length() > 0)
                  {
                     if (!requestContext.getUriInfo().getPath().contains("upload"))
                     {
                        traccia.put("contenuto", entityBody);
                        sb.append(" - Entity: ").append(traccia.get("contenuto"));
                     }
                     else
                     {
                        Integer length = entityBody.length();
                        traccia.put("contenuto", "Upload " + length + " bytes");
                        sb.append(" - Entity with file lenght: ").append(length);
                     }
                  }
               }
            }
            catch (Exception e)
            {

            }
         }
         if (isRestTrace())
         {
            logger.info("HTTP REQUEST : " + sb.toString());
         }
         requestContext.setProperty("traccia", traccia);
      }
      catch (Exception e)
      {
         logger.error("HTTP REQUEST : " + e.getClass().getCanonicalName() + " - msg: " + e.getMessage(), e);
      }
   }

   public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException
   {
      try
      {
         Map<String, Object> traccia = (Map<String, Object>) requestContext.getProperty("traccia");
         if (traccia == null)
         {
            return;
         }
         save(traccia, responseContext);
      }
      catch (Exception e)
      {
         logger.error("HTTP RESPONSE : " + e.getClass().getCanonicalName() + " - msg: " + e.getMessage(), e);
      }
   }

   abstract protected boolean isRestTrace();

   abstract protected void save(Map<String, Object> traccia, ContainerResponseContext responseContext);

   private String getEntityBody(ContainerRequestContext requestContext)
   {

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      InputStream in = requestContext.getEntityStream();
      final StringBuilder b = new StringBuilder();
      try {
         IOUtils.copy(in, out);
         byte[] requestEntity = out.toByteArray();
         if (requestEntity.length == 0) {
            b.append("").append("\n");
         } else {
            b.append(new String(requestEntity)).append("\n");
         }
         requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
      } catch (IOException ex) {
//         Handle logging error
      }
      return b.toString();

//      try
//      {
//         String json = IOUtils.toString(requestContext.getEntityStream(), Charsets.UTF_8);
//         // replace input stream as we've already read it
//         InputStream in = IOUtils.toInputStream(json);
//         requestContext.setEntityStream(in);
//         return json;
//      }
//      catch (IOException ex)
//      {
//         // Handle logging error
//         return "";
//      }
   }

}