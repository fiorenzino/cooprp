package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.model.Location;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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

   }
}

