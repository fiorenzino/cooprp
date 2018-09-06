package it.coopservice.cooprp.service.rs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Location;
import it.coopservice.cooprp.repository.LocationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.auth.cookie.annotation.AccountCookieVerification;
import org.giavacms.commons.auth.jwtcookie.annotation.AccountCookieAndTokenVerification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AppConstants.LOCATIONS_PATH)
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccountCookieVerification
public class LocationsRepositoryRs extends RsRepositoryService<Location>
{
   public LocationsRepositoryRs()
   {

   }

   @Inject
   public LocationsRepositoryRs(LocationsRepository locationRepository)
   {
      super(locationRepository);
   }

   ///In mapping frameworks spatial coordinates are often in order of latitude and longitude. In spatial databases spatial coordinates are in x = longitude, and y = latitude.
   @Override protected void postUpdate(Location object) throws Exception
   {
      int x = Integer.parseInt(object.longitudine);
      int y = Integer.parseInt(object.latitudine);
      object.location = new GeometryFactory().createPoint(new Coordinate(x, y));
   }

   @Override protected void postPersist(Location object) throws Exception
   {
      int x = Integer.parseInt(object.longitudine);
      int y = Integer.parseInt(object.latitudine);
      object.location = new GeometryFactory().createPoint(new Coordinate(x, y));
      super.postPersist(object);
   }


}

