package org.giavacms.core.util;

import java.text.*;
import java.util.Locale;

/**
 * @author
 * 
 */
public class FormatUtils
{

   public static DateFormat gg_mm_aaaa = new SimpleDateFormat("dd.MM.yyyy");
   public static DateFormat mm_aaaa = new SimpleDateFormat("MM.yyyy");
   public static DateFormat aaaa = new SimpleDateFormat("yyyy");
   public static DateFormat MMMM_aaaa = new SimpleDateFormat("MMMM yyyy", Locale.ITALY);

   /**
    * [3:19:16 PM] flower: DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(currentLocale);
    * unusualSymbols.setDecimalSeparator('|'); unusualSymbols.setGroupingSeparator('^');
    * 
    * String strange = "#,##0.###"; DecimalFormat weirdFormatter = new DecimalFormat(strange, unusualSymbols);
    * weirdFormatter.setGroupingSize(4);
    * 
    * String bizarre = weirdFormatter.format(12345.678); System.out.println(bizarre);
    */
   public static NumberFormat getMoney()
   {
      DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
      unusualSymbols.setDecimalSeparator(',');
      unusualSymbols.setGroupingSeparator('.');
      String strange = "#,##0.00";
      return new DecimalFormat(strange, unusualSymbols);
   }

   /**
    * @return
    */
   public static NumberFormat getMoneyWithoutGrouping()
   {
      DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
      unusualSymbols.setDecimalSeparator(',');
      unusualSymbols.setGroupingSeparator('.');
      String strange = "#0.00";
      return new DecimalFormat(strange, unusualSymbols);
   }

   public static NumberFormat getPercent()
   {
      DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
      unusualSymbols.setDecimalSeparator(',');
      String strange = "#0.###";
      return new DecimalFormat(strange, unusualSymbols);
   }

   public static String trimNonNull(Object in)
   {
      return in == null ? "" : in instanceof String ? ((String) in).trim()
               : in.toString();
   }

   public static String trimNonNull(String separator, String... in)
   {
      if (separator == null)
      {
         separator = "";
      }
      if (in == null || in.length == 0)
      {
         return "";
      }
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < in.length; i++)
      {
         in[i] = trimNonNull(in[i]);
         sb.append(in[i].length() > 0 ? separator : "").append(in[i]);
      }
      return sb.length() == 0 ? "" : sb.substring(separator.length());
   }

   public static boolean allNonNull(String... in)
   {
      if (in == null || in.length == 0)
      {
         return false;
      }
      for (int i = 0; i < in.length; i++)
      {
         if (trimNonNull(in[i]).length() == 0)
         {
            return false;
         }
      }
      return true;
   }

   public static void main(String[] args)
   {
      System.out.println(getMoney().format(4000));
   }
}
