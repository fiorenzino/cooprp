package org.giavacms.core.util;

import org.jboss.logging.Logger;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("restriction")
public class PasswordUtils implements Serializable
{

   static Logger logger = Logger.getLogger(PasswordUtils.class);

   private static final long serialVersionUID = 1L;

   public static String createPassword(String pwd)
   {
      return createPasswordWithHashAlgorithm(pwd, "SHA-256");
   }

   public static String createPasswordWithHashAlgorithm(String pwd,
            String algorithm)
   {
      MessageDigest md;
      try
      {
         // es MD5, SHA-256
         md = MessageDigest.getInstance(algorithm);
         md.update(pwd.getBytes());
         byte[] enc = md.digest();

         // Encode bytes to base64 to get a string
         //         String encode = new BASE64Encoder().encode(enc);
         String encode = new String(java.util.Base64.getMimeEncoder().encode(enc));
         logger.info(encode);
         return encode;
      }
      catch (NoSuchAlgorithmException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return null;
      }
   }

   public static void main(String[] args)
   {
      logger.info(createPassword("admin"));
      logger.info(createPassword("admin"));
   }
}
