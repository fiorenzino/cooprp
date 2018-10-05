package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.model.Notification;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Map;

@Stateless
@LocalBean
public class NotificationsRepository extends BaseRepository<Notification>
{
   @Override protected void applyRestrictions(Search<Notification> search, String alias, String separator,
            StringBuffer sb, Map<String, Object> params) throws Exception
   {
      super.applyRestrictions(search, alias, separator, sb, params);
      {
         sb.append(separator).append(" ").append(alias).append(".attivo = :attivo ");
         params.put("attivo", search.getObj().attivo);
         separator = " and ";
      }
   }
}
