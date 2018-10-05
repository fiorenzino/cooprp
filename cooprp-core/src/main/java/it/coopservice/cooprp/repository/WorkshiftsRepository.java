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
      {
         sb.append(separator).append(" ").append(alias).append(".attivo = :attivo ");
         params.put("attivo", search.getObj().attivo);
         separator = " and ";
      }
   }

   @Override public void delete(Object key) throws Exception
   {
      int updated = getEm().createQuery("UPDATE " + Workshift.class.getSimpleName() +
               " SET attivo = FALSE " +
               " WHERE attivo = :UUID ")
               .setParameter("UUID", key)
               .executeUpdate();
      if (updated < 1)
      {
         throw new Exception("Non esiste un record con uuid: " + key);
      }

      if (updated > 1)
      {
         throw new Exception("Esistono diversi record con uuid: " + key);
      }
   }
}
