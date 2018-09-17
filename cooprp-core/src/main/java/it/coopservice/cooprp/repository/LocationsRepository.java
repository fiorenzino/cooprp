package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppConstants;
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

   public Location findLocation(String latitudine, String longitudine)
   {
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

