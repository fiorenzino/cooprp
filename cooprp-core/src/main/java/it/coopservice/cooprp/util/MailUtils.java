package it.coopservice.cooprp.util;


import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.ucsmail.service.ws.*;
import org.giavacms.api.management.AppConstants;
import org.jboss.logging.Logger;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.*;

/**
 * Created by fiorenzo on 11/05/16.
 */
public class MailUtils
{

   static Logger logger = Logger.getLogger(MailUtils.class.getName());

   public static Long send(String subject, String body, Set<String> tos) throws Exception
   {
      return sendWithInfo(subject, body, tos, null, null);
   }

   public static Long send(String subject, String body, Set<String> tos, Set<String> ccs) throws Exception
   {
      return sendWithInfo(subject, body, tos, ccs, null);
   }

   public static Long sendWithInfo(String subject, String body, Set<String> tos, Set<String> ccs, byte[] csv)
            throws Exception
   {
      if (tos == null || tos.size() == 0)
      {
         // return -1L;
         throw new Exception("destinatari email vuoti");
      }
      List<String> tosList = new ArrayList<>();
      for (String value : tos)
      {
         tosList.add(value.replace(" ", "").toLowerCase());
      }
      List<String> ccsList = null;
      if (ccs != null && !ccs.isEmpty())
      {
         ccsList = new ArrayList<>();
         for (String value : ccs)
         {
            ccsList.add(value.replace(" ", "").toLowerCase());
         }
      }

      MailWS mailWsPort = null;
      try
      {
         mailWsPort = new MailWSService(new URL(AppProperties.ucsmailWsdlLocation.value())).getMailWSPort();
         Map<String, Object> rContext = ((BindingProvider) mailWsPort)
                  .getRequestContext();
         rContext.put(BindingProvider.USERNAME_PROPERTY, AppProperties.ucsmailUser.value());
         rContext.put(BindingProvider.PASSWORD_PROPERTY, AppProperties.ucsmailPassword.value());
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
         // throw new Exception("Errore nella generazione client a ws mail");
         logger.error("cannot send '" + subject + "' - '" + body + "' --> Errore nella generazione client a ws mail");
         // return -2L;
         throw new Exception("errore nella generazione del ws client");
      }
      try
      {
         String[] bcc = AppProperties.ucsMailBcc.split(AppConstants.EMAIL_SEPARATOR);
         MailPriority mailPriority = MailPriority.NORMAL;
         MailFormat mailFormat = MailFormat.TXT;
         String receiptEmail = null;
         String receiptUrl = null;

         String accountName = AppProperties.ucsMailAccount.value();
         String from = "noreply@coopservice.it";
         String deliveryNotificationTo = null;
         UcsAttachment[] ucsAttachments = null;
         if (csv != null)
         {
            UcsAttachment ucsAttachment = new UcsAttachment();
            ucsAttachment.setName("report.csv");
            ucsAttachment.setContent(csv);
            ucsAttachments = new UcsAttachment[] { ucsAttachment };
         }
         logger.info("Sending mail: '" + subject + "' to: " + tos);

         Long ucsMailId = mailWsPort.sendMessage(accountName, from, subject, body, tosList,
                  Arrays.asList(bcc),
                  ccsList,
                  ucsAttachments != null ? Arrays.asList(ucsAttachments) : null,
                  deliveryNotificationTo, mailPriority, mailFormat, receiptEmail, receiptUrl);

         logger.info("inviata mail " + ucsMailId + " a " + Arrays.asList(tos));
         return ucsMailId;
      }
      catch (Throwable t)
      {
         logger.error(t.getMessage(), t);
         logger.error("cannot send '" + subject + "' - '" + body + "' --> Errore generico.");
         throw new Exception("errore generico in fase di invio");
      }
   }
}
