package it.coopservice.cooprp.service.rs;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.Location;
import it.coopservice.cooprp.repository.LocationsRepository;
import org.giavacms.api.service.RsRepositoryService;
import org.giavacms.commons.jwt.annotation.AccountTokenVerification;

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
@AccountTokenVerification
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

}

