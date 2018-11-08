package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.Privacy;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
public class PrivaciesRepository extends BaseRepository<Privacy>
{

   @Override protected void applyRestrictions(Search<Privacy> search, String alias, String separator,
            StringBuffer sb, Map<String, Object> params) throws Exception
   {
      {
         sb.append(separator).append(" ").append(alias).append(".attivo = :attivo ");
         params.put("attivo", search.getObj().attivo);
         separator = " and ";
      }
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public boolean updateAccepted(String codiceFiscale) throws Exception
   {
      String query = ""
               + " UPDATE " + AppProperties.defaultSchema.value() + Privacy.TABLE_NAME
               + " SET ACCETTATO = TRUE, LASTUPDATE = :DATE_UPDATED "
               + " WHERE CODICEFISCALE = :CODICE_FISCALE "
               + " AND ATTIVO = TRUE ";

      int updated = getEm().createNativeQuery(query, String.class)
               .setParameter("CODICE_FISCALE", codiceFiscale)
               .setParameter("DATE_UPDATED", new Date())
               .executeUpdate();
      if (updated > 1)
      {
         logger.warn("MORE THAN ONE PER CODICE FICALE: " + codiceFiscale);
      }

      if (updated > 0)
      {
         return true;
      }
      else
      {
         Privacy privacy = new Privacy();
         privacy.codiceFiscale = codiceFiscale;
         privacy.lastUpdate = new Date();
         privacy.accettato = true;
         privacy.attivo = true;
         Privacy persisted = persist(privacy);
         logger.info(" persited new entity with uuid " + persisted.uuid + " for CF: " + persisted.codiceFiscale);
         return true;
      }

   }

}
