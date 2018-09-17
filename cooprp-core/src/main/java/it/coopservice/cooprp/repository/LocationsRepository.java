package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.Location;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Map;

@Stateless
@LocalBean
public class LocationsRepository extends BaseRepository<Location>
{
   @Override
   protected void applyRestrictions(Search<Location> search, String alias, String separator,
            StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public String  findLocation(String latitudine, String longitudine)
   {
      DecimalFormat df = new DecimalFormat();
      DecimalFormatSymbols sfs = new DecimalFormatSymbols();
      sfs.setDecimalSeparator('.');
      df.setDecimalFormatSymbols(sfs);
      try
      {
         double x = df.parse(longitudine).doubleValue();
         double y = df.parse(latitudine).doubleValue();
         String queryString= ""
                  + " SELECT UUID "
                  + " from " + AppProperties.defaultSchema.value() + Location.TABLE_NAME
                  + " where ST_Contains (ST_Buffer( location, range, 'quad_segs=8'), POINT(:X_AXIS, :Y_AXIS)\\:\\:geometry)";

        return (String) getEm().createNativeQuery(queryString)
                  .setParameter("X_AXIS", x)
                  .setParameter("Y_AXIS", y)
                  .getSingleResult();

      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }

      //ST_Contains: Geometry A contains Geometry B if and only if no points of B lie in the exterior of A,
      //  and at least one point of the interior of B lies in the interior of A https://postgis.net/docs/ST_Contains.html
      //ST_Buffer:


      return null;
   }

   public void updateLocation(Location location) throws ParseException
   {
      DecimalFormat df = new DecimalFormat();
      DecimalFormatSymbols sfs = new DecimalFormatSymbols();
      sfs.setDecimalSeparator('.');
      df.setDecimalFormatSymbols(sfs);
      double x = df.parse(location.longitudine).doubleValue();
      double y = df.parse(location.latitudine).doubleValue();
      String queryString =
               " UPDATE " + AppProperties.defaultSchema.value() + Location.TABLE_NAME + " AS L " +
               " SET LOCATION = POINT(:LONGITUDE, :LATITUDE)\\:\\:geometry " +
               " WHERE UUID = :LOCATION_UUID";
      getEm().createNativeQuery(queryString)
               .setParameter("LONGITUDE", x)
               .setParameter("LATITUDE", y)
               .setParameter("LOCATION_UUID", location.uuid)
               .executeUpdate();
   }
}

