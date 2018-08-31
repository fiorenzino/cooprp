package org.giavacms.api.filter;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Provider
@PreMatching
public class RESTTraceFilter implements ContainerResponseFilter
{
   private final static Logger logger = Logger.getLogger(RESTTraceFilter.class);

   @PostConstruct
   public void init()
   {
      System.out.println("RESTTraceFilter");
   }

   @Override
   public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseCtx) throws IOException
   {

      System.out.println(requestContext.getUriInfo().getPath());
      //      String entityBody = getEntityBody(requestContext).trim();
      //      System.out.println(entityBody);
      /*
      Traccia traccia = new Traccia();
      traccia.setDataora(new Date());
      StringBuffer sb = new StringBuffer();
      try
      {
         traccia.setUtente(requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown"
                  : requestContext.getSecurityContext().getUserPrincipal().getName());
         sb.append("User: ").append(traccia.getUtente());

         String path = requestContext.getUriInfo().getPath();
         String[] split = requestContext.getUriInfo().getPath().split("/");
         if (split.length >= 3)
         {
            traccia.setServizio(split[2]);
            traccia.setOperazione(path.substring(1 + split[1].length() + 1 + split[2].length()));
         }
         else
         {
            traccia.setServizio(requestContext.getUriInfo().getPath());
            traccia.setOperazione("");
         }
         sb.append(" - Path: ").append(path);

         traccia.setAgente(requestContext.getHeaderString("User-Agent"));
         sb.append(" - Headers: ").append(requestContext.getHeaders());

         traccia.setMetodo(requestContext.getMethod());
         sb.append(" - Method: ").append(traccia.getMetodo());

         traccia.setStato(responseCtx.getStatus());
         try
         {
            if (MediaType.APPLICATION_FORM_URLENCODED_TYPE.equals(requestContext.getMediaType()))
            {

               traccia.setContenuto(
                        ((org.jboss.resteasy.specimpl.RequestImpl) requestContext.getRequest())
                                 .getFormParameters() + ""
               );
               sb.append(" - Params: ").append(traccia.getContenuto());
            }
            else
            {
               String entityBody = getEntityBody(requestContext).trim();
               if (entityBody.length() > 0)
               {
                  if (!requestContext.getUriInfo().getPath().contains("upload"))
                  {
                     traccia.setContenuto(entityBody);
                     sb.append(" - Entity: ").append(traccia.getContenuto());
                  }
                  else
                  {
                     Integer length = entityBody.length();
                     traccia.setContenuto("Upload " + length + " bytes");
                     sb.append(" - Entity with file lenght: ").append(length);
                  }
               }
            }
         }
         catch (Exception e)
         {

         }

         if (AppProperties.restTrace.cast(Boolean.class).booleanValue())
         {
            logger.info("HTTP REQUEST : " + sb.toString());
         }


      }
      catch (Exception e)
      {
         logger.error("HTTP REQUEST : " + e.getClass().getCanonicalName() + " - msg: " + e.getMessage(), e);
      }
      finally
      {
         if (
            // non loggo le GET. inoffensive e tantissime
                  !AppConstants.GET_METHOD.equals(traccia.getMetodo())
                           &&
                           // non loggo le OPTIONS. inutili
                           !AppConstants.OPTIONS_METHOD.equals(traccia.getMetodo())
                           &&
                           // non loggo i login da gui
                           !AppConstants.LOGIN_CUSTOM_PATH.equals(traccia.getServizio())
                  )
         {
            BeanUtils.getBean(TracceRepository.class).persist_Async(traccia);
         }

         */

   }

   private String getEntityBody(ContainerRequestContext requestContext)
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      InputStream in = requestContext.getEntityStream();

      final StringBuilder b = new StringBuilder();
      try
      {
         IOUtils.copy(in, out);
         byte[] requestEntity = out.toByteArray();
         if (requestEntity.length == 0)
         {
            b.append("").append("\n");
         }
         else
         {
            b.append(new String(requestEntity)).append("\n");
         }
         requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));

      }
      catch (IOException ex)
      {
         // Handle logging error
      }
      return b.toString();
   }

}