package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.Operation;
import org.giavacms.api.repository.Search;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Map;

@Stateless
@LocalBean
public class OperationsRepository extends BaseRepository<Operation>
{
   @Override protected void applyRestrictions(Search<Operation> search, String alias, String separator, StringBuffer sb,
            Map<String, Object> params) throws Exception
   {
      super.applyRestrictions(search, alias, separator, sb, params);
   }

   public int updateLocation(String operationUuid, String locationUuid)
   {
      String queryString = ""
               + " UPDATE " + AppProperties.defaultSchema.value() + Operation.TABLE_NAME
               + " SET location_uuid = :LOCATION_UUID"
               + " WHERE uuid = :OPERATION_UUID";

      int result = getEm().
               createNativeQuery(queryString)
               .setParameter("OPERATION_UUID", operationUuid)
               .setParameter("LOCATION_UUID", locationUuid)
               .executeUpdate();
      return result;
   }

   public int updateLocationAndDeleteCoordinates(String operationUuid, String locationUuid)
   {
      String queryString = ""
               + " UPDATE " + AppProperties.defaultSchema.value() + Operation.TABLE_NAME
               + " SET location_uuid = :LOCATION_UUID, latitudine = null, longitudine = null"
               + " WHERE uuid = :OPERATION_UUID";

      int result = getEm().
               createNativeQuery(queryString)
               .setParameter("OPERATION_UUID", operationUuid)
               .setParameter("LOCATION_UUID", locationUuid)
               .executeUpdate();
      return result;
   }


   public int deleteCoordinates(String operationUuid)
   {
      String queryString = ""
               + " UPDATE " + AppProperties.defaultSchema.value() + Operation.TABLE_NAME
               + " SET latitudine = null, longitudine = null"
               + " WHERE uuid = :OPERATION_UUID";

      int result = getEm().
               createNativeQuery(queryString)
               .setParameter("OPERATION_UUID", operationUuid)
//               .setParameter("LOCATION_UUID", locationUuid)
               .executeUpdate();
      return result;
   }
}
