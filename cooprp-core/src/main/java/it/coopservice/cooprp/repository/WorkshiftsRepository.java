package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.model.Workshift;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Map;

@Stateless
@LocalBean
public class WorkshiftsRepository extends BaseRepository<Workshift>
{
   @Override protected void applyRestrictions(Search<Workshift> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      super.applyRestrictions(search, alias, separator, sb, params);
   }
}
