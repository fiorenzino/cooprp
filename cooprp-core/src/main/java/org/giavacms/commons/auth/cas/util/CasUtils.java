package org.giavacms.commons.auth.cas.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CasUtils
{

   public static String getCasServiceCallbackUrl(HttpServletRequest request)
   {
      String serverName = request.getServerName() + ":"
               + request.getServerPort();
      return getService(request, serverName);
   }

   private static String getService(HttpServletRequest request, String server)
   {
      if (server == null)
      {
         throw new IllegalArgumentException("name of server is required");
      }

      StringBuffer sb = new StringBuffer();
      if (request.isSecure())
      {
         sb.append("https://");
      }
      else
      {
         String scheme = request.getScheme();
         if (scheme == null)
            scheme = "http";
         sb.append(scheme + "://");
      }
      sb.append(server);
      sb.append(request.getRequestURI());

      if (request.getQueryString() != null)
      {
         int ticketLoc = request.getQueryString().indexOf("ticket=");

         if (ticketLoc == -1)
         {
            sb.append("?" + request.getQueryString());
         }
         else if (ticketLoc > 0)
         {
            ticketLoc = request.getQueryString().indexOf("&ticket=");
            if (ticketLoc == -1)
            {
               sb.append("?" + request.getQueryString());
            }
            else if (ticketLoc > 0)
            {
               sb.append("?"
                        + request.getQueryString().substring(0, ticketLoc));
            }
         }
      }

      return sb.toString();
   }

   public static String safeGetParameter(final HttpServletRequest request, final String parameter)
   {
      if ("POST".equals(request.getMethod()) && "logoutRequest".equals(parameter))
      {
         return request.getParameter(parameter);
      }
      return request.getQueryString() == null || request.getQueryString().indexOf(parameter) == -1 ? null : request
               .getParameter(parameter);
   }

   public static String getRedirectUrl(final String casServerLoginUrl, final String serviceParameterName,
            final String serviceUrl)
   {
      try
      {
         return casServerLoginUrl + (casServerLoginUrl.indexOf("?") != -1 ? "&" : "?") + serviceParameterName + "="
                  + URLEncoder.encode(serviceUrl, "UTF-8");
      }
      catch (final UnsupportedEncodingException e)
      {
         throw new RuntimeException(e);
      }
   }

}
