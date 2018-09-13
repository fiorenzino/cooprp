package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.Operation;
import org.giavacms.api.repository.Search;
import org.giavacms.core.util.DateUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

@Stateless
@LocalBean
public class OperationsRepository extends BaseRepository<Operation>
{
   @Override protected void applyRestrictions(Search<Operation> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public int clanupOperation(int retentionPeriod, String unitOfMeasure)
   {

      String queryStirng = " DELETE FROM " + AppProperties.defaultSchema.value() + Operation.TABLE_NAME
               + " WHERE realTime <= :RELATIME_TO_DELETE ";

      Calendar calendar = new GregorianCalendar();
      calendar.add(Calendar.DAY_OF_MONTH, -retentionPeriod);

      getEm().createNativeQuery(queryStirng).setParameter("RELATIME_TO_DELETE", DateUtils.convertCalendar(calendar,
               TimeZone.getTimeZone(AppConstants.GMT_TIMEZONE))).executeUpdate();

      return 0;
   }
}
