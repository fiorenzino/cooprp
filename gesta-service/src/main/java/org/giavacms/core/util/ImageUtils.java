package org.giavacms.core.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ImageUtils
{

   private static final String TO_REMOVE_PNG = "data:image/png;base64,";
   private static final String TO_REMOVE_JPEG = "data:image/jpeg;base64,";

   static Logger logger = Logger.getLogger(ImageUtils.class);

   public static String encodeBase64(File file) throws Exception
   {
      return encodeBase64(file.getAbsolutePath(), org.apache.commons.io.FileUtils
               .readFileToByteArray(file));
   }

   public static String encodeBase64(String filename, byte[] bytes) throws Exception
   {
      String enarr = Base64.encodeBase64String(bytes);
      return (filename.toLowerCase().endsWith(".png") ? TO_REMOVE_PNG : TO_REMOVE_JPEG) + enarr;
   }

   public static File readFileBytes(MultipartFormDataInput multipartFormDataInput)
            throws Exception
   {
      File file = null;

      Map<String, List<InputPart>> formParts = multipartFormDataInput.getFormDataMap();
      List<InputPart> fileParts = formParts.get("file");
      for (InputPart filePart : fileParts)
      {
         // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
         MultivaluedMap<String, String> headers = filePart.getHeaders();
         String fileName = FileUtils.getLastPartOf(HttpUtils.parseFileName(headers));
         InputStream istream = filePart.getBody(InputStream.class, null);
         byte[] byteArray = IOUtils.toByteArray(istream);
         file = new File(fileName);
         org.apache.commons.io.FileUtils.writeByteArrayToFile(file, byteArray);
      }
      return file;
   }

}
