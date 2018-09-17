package it.coopservice.cooprp.service.rs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Location;
import it.coopservice.cooprp.repository.LocationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.cookie.annotation.AccountCookieVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Path(AppConstants.LOCATIONS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountCookieVerification
public class LocationsRepositoryRs extends RsRepositoryService<Location>
{

   @Inject
   LocationsRepository locationsRepository;
   public LocationsRepositoryRs()
   {

   }

   @Inject
   public LocationsRepositoryRs(LocationsRepository locationRepository)
   {
      super(locationRepository);
   }

   ///In mapping frameworks spatial coordinates are often in order of latitude and longitude.
   // In spatial databases spatial coordinates are in x = longitude, and y = latitude.
    @Override protected Location preUpdate(Location object) throws Exception
   {
      DecimalFormat df = new DecimalFormat();
      DecimalFormatSymbols sfs = new DecimalFormatSymbols();
      sfs.setDecimalSeparator('.');
      df.setDecimalFormatSymbols(sfs);
      double x = df.parse(object.longitudine).doubleValue();
      double y = df.parse(object.latitudine).doubleValue();
      Coordinate coordinate = new Coordinate(x, y);
      Point point = new GeometryFactory().createPoint(coordinate);
      //      object.location = point;
      return object;
   }

   @Override protected void prePersist(Location object) throws Exception
   {
      DecimalFormat df = new DecimalFormat();
      DecimalFormatSymbols sfs = new DecimalFormatSymbols();
      sfs.setDecimalSeparator('.');
      df.setDecimalFormatSymbols(sfs);
      double x = df.parse(object.longitudine).doubleValue();
      double y = df.parse(object.latitudine).doubleValue();
      Coordinate coordinate = new Coordinate(x, y);
      Point point = new GeometryFactory().createPoint(coordinate);
      object.location = point;
   }
}

