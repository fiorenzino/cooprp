package org.giavacms.commons.util;

/**
 * Created by fiorenzo on 05/12/15.
 */

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

public class WebTargetClosable implements AutoCloseable
{
   public WebTarget webTarget;
   private Client client;
   public Response response;

   public WebTargetClosable()
   {

   }

   public Client ignoreSSLClient() throws Exception
   {

      SSLContext sslcontext = SSLContext.getInstance("TLS");

      sslcontext.init(null, new TrustManager[] { new X509TrustManager()
      {
         public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
         {
         }

         public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
         {
         }

         public X509Certificate[] getAcceptedIssuers()
         {
            return new X509Certificate[0];
         }
      } }, new java.security.SecureRandom());

      return ClientBuilder.newBuilder()
               .sslContext(sslcontext)
               .hostnameVerifier((s1, s2) -> true)
               .build();
   }

   /*
    * targetHost + targetPath togheter
    */
   public WebTargetClosable(String targetHostPath) throws Exception
   {
      if (targetHostPath == null || targetHostPath.trim().isEmpty())
         throw new Exception("targetHostPath is null");
      //      client = new ResteasyClientBuilder()
      //         .disableTrustManager()
      //         .socketTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
      //               .build();
      webTarget = ignoreSSLClient().target(targetHostPath);
   }

   /*
    * if targetHost is null or empty, we use AppProperties.baseUrl
    */
   public WebTargetClosable(String targetHost, String targetPath) throws Exception
   {
      if (targetPath == null || targetPath.trim().isEmpty())
         throw new Exception("targetPath is null");
      //      client = new ResteasyClientBuilder().disableTrustManager()
      //               .build();
      if (targetHost == null || targetHost.isEmpty())
         throw new Exception("targetHost is null");
      webTarget = ignoreSSLClient().target(targetHost + targetPath);
   }

   @Override
   public void close() throws Exception
   {
      // System.out.println("WebTargetClosable CLOSE");
      if (response != null)
      {
         response.close();
      }
      if (client != null)
      {
         client.close();
      }
   }

}