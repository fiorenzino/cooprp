package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.Language;
import it.coopservice.cooprp.model.pojo.LanguageStatus;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
public class LanguagesRepository extends BaseRepository<Language>
{
   @Override
   protected void applyRestrictions(Search<Language> search, String alias, String separator,
            StringBuffer sb,
            Map<String, Object> params) throws Exception
   {

   }

   @Override protected Language prePersist(Language object) throws Exception
   {
      object.lastUpdate = new Date();
      return super.prePersist(object);
   }

   @Override protected Language preUpdate(Language object) throws Exception
   {
      object.lastUpdate = new Date();
      return super.preUpdate(object);
   }

   public List<LanguageStatus> getLangugaesStatus()
   {

      List<LanguageStatus> resultList = new ArrayList<>();
      List<Object> list = getEm().createNativeQuery(
               " SELECT language, max(lastUpdate)"
                        + " FROM " + AppProperties.defaultSchema.value() + Language.TABLE_NAME
                        + " GROUP BY language "
      ).getResultList();

      for (Object array : list)
      {
         Object[] realArray = (Object[]) array;
         LanguageStatus status = new LanguageStatus();
         status.language = (String) realArray[0];
         status.lastUpdate = (Date) realArray[1];
         resultList.add(status);
      }
      return resultList;
   }


   public JsonObject getLanguageMap(String language)
   {

      List<LanguageStatus> resultList = new ArrayList<>();
      List<Object> list = getEm().createNativeQuery(
               " SELECT key, value"
                        + " FROM " + AppProperties.defaultSchema.value() + Language.TABLE_NAME
                        + " WHERE language = :LANGUAGE  "
      ).setParameter("LANGUAGE", language).getResultList();

      JsonObjectBuilder builder = Json.createObjectBuilder();
      for (Object array : list)
      {
         Object[] realArray = (Object[]) array;
         builder.add((String)realArray[0], (String)realArray[1]);
      }
      return builder.build();
   }


}
