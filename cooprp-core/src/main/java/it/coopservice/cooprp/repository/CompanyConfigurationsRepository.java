package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.CompanyConfiguration;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
public class CompanyConfigurationsRepository extends BaseRepository<CompanyConfiguration>
{
   @Override
   protected void applyRestrictions(Search<CompanyConfiguration> search, String alias, String separator,
            StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      super.applyRestrictions(search, alias, separator, sb, params);

   }

   public String findBySocietaId(String societaId) throws Exception
   {
      String queryString = ""
               + " SELECT UUID "
               + " FROM " + AppProperties.defaultSchema.value() + CompanyConfiguration.TABLE_NAME
               + " WHERE societaId = :SOCIETA_ID";

      List<String> resultList = getEm().
               createNativeQuery(queryString)
               .setParameter("SOCIETA_ID", societaId)
               .getResultList();
      if (resultList == null || resultList.isEmpty())
      {
         throw new Exception("Non esiste una Configurazione per societaId " + societaId);
      }
      if (resultList.size() > 1)
      {
         logger.warn("MORE THAN ONE COMPANY CONFIGURATION FOR A SINGLE SOCIETAID: " + societaId);
      }
      return resultList.get(0);
   }

}
