package org.giavacms.api.repository;

import org.giavacms.api.util.RepositoryUtils;
import org.giavacms.commons.util.DateUtils;
import org.jboss.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @param <T>
 * @author fiorenzo pizza
 */
public abstract class AbstractRepository<T> implements Serializable,
         Repository<T>
{

   private static final long serialVersionUID = 1L;

   // --- JPA ---------------------------------

   /**
    * @return
    */
   protected abstract EntityManager getEm();

   public abstract void setEm(EntityManager em);

   // --- Logger -------------------------------

   protected final Logger logger = Logger.getLogger(getClass().getName());

   // --- Mandatory logic --------------------------------

   // protected abstract Class<T> getEntityType();
   @SuppressWarnings("unchecked")
   protected Class<T> getEntityType() throws Exception
   {
      ParameterizedType parameterizedType = (ParameterizedType) getClass()
               .getGenericSuperclass();
      return (Class<T>) parameterizedType.getActualTypeArguments()[0];
   }

   // --- CRUD --------------

   public T create(Class<T> domainClass) throws Exception
   {
      return domainClass.newInstance();
   }

   public T persist(T object) throws Exception
   {
      object = prePersist(object);
      if (object != null)
      {
         getEm().persist(object);
      }
      return object;
   }

   public void detach(T object)
   {
      if (object != null)
      {
         getEm().detach(object);
      }
   }

   protected T prePersist(T object) throws Exception
   {
      return object;
   }

   public T find(Object key) throws Exception
   {
      return getEm().find(getEntityType(), key);
   }

   public T fetch(Object key) throws Exception
   {
      return getEm().find(getEntityType(), key);
   }

   public T update(T object) throws Exception
   {
      object = preUpdate(object);
      getEm().merge(object);
      return object;
   }

   protected T preUpdate(T object) throws Exception
   {
      return object;
   }

   public void delete(Object key) throws Exception
   {
      T obj = getEm().find(getEntityType(), key);
      try
      {
         Field activeField = getEntityType().getDeclaredField("active");
         activeField.setAccessible(true);
         activeField.set(obj, false);
      }
      catch (Exception e)
      {
         getEm().remove(obj);
      }
   }

   // --- LIST ------------------------------------------

   @SuppressWarnings("unchecked")
   public List<T> getList(Search<T> search, int startRow, int pageSize)
            throws Exception
   {
      List<T> result = null;
      boolean count = false;
      Query res = getRestrictions(search, count);
      if (res == null)
         return result;
      if (startRow >= 0)
      {
         res.setFirstResult(startRow);
      }
      if (pageSize > 0)
      {
         res.setMaxResults(pageSize);
      }

      return (List<T>) res.getResultList();
   }

   public int getListSize(Search<T> search) throws Exception
   {
      boolean count = true;
      Query res = getRestrictions(search, count);
      return ((Long) res.getSingleResult()).intValue();
   }

   /**
    * @param startRow
    * @param pageSize
    * @param res
    * @return
    */
   @SuppressWarnings("unchecked")
   public List<T> getList(String query, Map<String,Object> params, int startRow, int pageSize, boolean isNative)
            throws Exception
   {
      try
      {
         Query res = null;
         if ( isNative ) {
            res = getEm().createNativeQuery(query);
         } else {
            res = getEm().createQuery(query);
         }
         for ( String key : params.keySet() ) {
            res.setParameter(key, params.get(key));
         }
         List<T> result = new ArrayList<T>();
         if (startRow >= 0)
         {
            res.setFirstResult(startRow);
         }
         if (pageSize > 0)
         {
            res.setMaxResults(pageSize);
         }
         result = (List<T>) res.getResultList();
         if (result != null)
            return result;
      }
      catch (Exception e)
      {
         logger.info(e.getMessage());
      }
      return new ArrayList<T>();
   }

   /**
    * criteri di default, comuni a tutti, ma specializzabili da ogni EJB tramite overriding
    */
   protected Query getRestrictions(Search<T> search, boolean justCount)
            throws Exception
   {

      Map<String, Object> params = new HashMap<String, Object>();
      String alias = "c";
      StringBuffer sb = new StringBuffer(getBaseList(search.getObj()
               .getClass(), alias, justCount));
      String separator = " where ";

      applyRestrictions(search, alias, separator, sb, params);

      if (!justCount)
      {
         sb.append(getOrderBy(alias, search.getOrder()));
      }

      Query q = getEm().createQuery(sb.toString());
      for (String param : params.keySet())
      {
         q.setParameter(param, params.get(param));
      }

      return q;

   }

   /**
    * metodo da sovrascrivere per applicare parametri alla query, con relative condizioni d'uso
    * <p/>
    * esempio:
    * <p/>
    * String leftOuterJoinAlias = "s"; if (search.getObj().getNumero() != null &&
    * search.getObj().getNumero().trim().length() > 0) { sb.append(" left outer join ").append(alias)
    * .append(".serviziPrenotati ").append(leftOuterJoinAlias); // sb.append(" on "
    * ).append(leftOuterJoinAlias).append(".allegati.id = ").append (alias).append(".id"); }
    * <p/>
    * if (search.getObj().getAttivo() != null) { sb.append(separator).append(" ").append(alias)
    * .append(".attivo = :attivo "); // aggiunta alla mappa params.put("attivo", search.getObj().getAttivo()); //
    * separatore separator = " and "; }
    * <p/>
    * if (search.getObj().getNumero() != null && !search.getObj().getNumero().trim().isEmpty()) {
    * sb.append(separator).append(leftOuterJoinAlias) .append(".servizio.numero = :numero and ")
    * .append(leftOuterJoinAlias) .append(".servizio.tipo = :tipoServizio "); // aggiunta alla mappa
    * params.put("numero", search.getObj().getNumero()); params.put("tipoServizio", TipoServizioEnum.OMB); // separatore
    * separator = " and "; }
    *
    * @param search
    * @param alias
    * @param separator
    * @param sb
    * @param params
    */
   protected void _applyRestrictions(Search<T> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
            throws Exception
   {
   }

   protected String getBaseList(Class<? extends Object> clazz, String alias,
            boolean count) throws Exception
   {
      if (count)
      {
         return "select count(" + alias + ") from " + clazz.getSimpleName()
                  + " " + alias + " ";
      }
      else
      {
         return "select " + alias + " from " + clazz.getSimpleName() + " "
                  + alias + " ";
      }
   }

   protected abstract String getDefaultOrderBy();

   public String getOrderBy(String alias, String orderBy) throws Exception
   {
      try
      {
         if (orderBy == null || orderBy.length() == 0)
         {
            orderBy = getDefaultOrderBy();
         }
         StringBuffer result = new StringBuffer();
         String[] orders = orderBy.split(",");
         for (String order : orders)
         {
            result.append(", ").append(alias).append(".")
                     .append(order.trim()).append(" ");
         }
         return " order by " + result.toString().substring(2);
      }
      catch (Exception e)
      {
         return "";
      }
   }

   protected String likeParam(String param)
   {
      return "%" + param + "%";
   }

   protected String likeParamL(String param)
   {
      return "%" + param;
   }

   protected String likeParamR(String param)
   {
      return param + "%";
   }

   @Override
   public boolean exist(Object key) throws Exception
   {
      String alias = "c";
      String idFieldName = RepositoryUtils.getIdFieldName(getEntityType());
      boolean justCount = true;
      StringBuffer sb = new StringBuffer(getBaseList(getEntityType(), alias,
               justCount));
      sb.append(" where " + alias + "." + idFieldName + " = :ID");
      Query q = getEm().createQuery(sb.toString()).setParameter("ID", key);
      return ((Long) q.getSingleResult()).intValue() > 0;
   }

   /**
    * Override this is needed
    */
   public Object castId(String key) throws Exception
   {
      return RepositoryUtils.castId(key, getEntityType());
   }

   public String getUniqueKey(String key) throws Exception
   {
      String keyNotUsed = key;
      boolean found = false;
      int i = 0;
      while (!found)
      {
         logger.info("key to search: " + keyNotUsed);
         boolean exist = exist(keyNotUsed);
         logger.info("found " + exist + " pages with id: " + keyNotUsed);
         if (exist)
         {
            i++;
            keyNotUsed = key + "-" + i;
         }
         else
         {
            found = true;
            return keyNotUsed;
         }
      }
      return "";
   }

   protected void applyRestrictions(Search<T> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
            throws Exception
   {

      // handling custom ways of restrict search first (left join fetches, non-standard queries filter, subqueries, ...)
      // customRestrictions search result should be null if no standard filter is added
      // left join fetches and alias in the beginning can be add as a side-effect to the stringbuffer object
      Search<T> customSearch = customRestrictions(search, alias, separator, sb, params);
      if (customSearch != null)
      {
         separator = " and ";
      }

      Map<String, Method> methods = new HashMap<>();
      for (Method method : getEntityType().getDeclaredMethods())
      {
         if (method.getName().startsWith("get") || method.getName().startsWith("is"))
         {
            methods.put(method.getName().toLowerCase(), method);
         }
      }

      Field[] fields = search.getClass().getDeclaredFields();

      for (Field searchField : fields)
      {

         String searchKind = searchField.getName();

         switch (searchKind)
         {
         case "obj":
         case "like":
         case "not":
         case "from":
         case "to":
         case "nil":
         case "notNil":
            break;
         default:
            continue;
         }

         // e.g.: search.obj
         searchField.setAccessible(true);
         Object searchFieldInstance = searchField.get(search);

         for (Field valueField : searchFieldInstance.getClass().getDeclaredFields())
         {

            valueField.setAccessible(true);

            Class valueClass = valueField.getType();
            String valueName = valueField.getName();

            // skipping static fields of search.obj
            if (Modifier.isStatic(valueField.getModifiers()))
            {
               continue;
            }

            // search.obj.someOtherMappedEntity
            if (valueClass.getAnnotation(Entity.class) != null)
            {
               // we leave entity type subsearches to custom restrictions
               continue;
            }

            // search.obj.someTransientValue
            if (valueField.getAnnotation(Transient.class) != null)
            {
               continue;
            }
            else
            {
               Method getter = methods.get("get" + valueName.toLowerCase());
               if (getter == null)
               {
                  getter = methods.get("is" + valueName.toLowerCase());
               }
               if (getter != null && getter.getAnnotation(Transient.class) != null)
               {
                  // we leave transient object searches to custom restrictions
                  continue;
               }
            }

            // skip custom restrictions
            if (customSearch != null && isRelevant(valueField.get(searchField.get(customSearch))))
            {
               continue;
            }

            //skippo i primitivi, ma li dovrei skippare solo se hanno valore di default
            if (valueClass.isPrimitive()) {
               continue;
            }
            // cablone (o custom restrictions) per le variabili boolean preinizializzate a true
            if (valueName.equals("active"))
            {
               if (searchKind.equals("obj"))
               {
                  sb.append(separator).append(alias).append(".active = :obj_active ");
                  params.put("obj_active", true);
                  separator = " and ";
               }
               continue;
            }

            // e.g. search.obj.sticazzi_uuid
            Object value = valueField.get(searchFieldInstance);
            if (!isRelevant(value))
            {
               continue;
            }

            switch (searchKind)
            {
            case "like":
               if (!valueClass.getCanonicalName().equals(String.class.getCanonicalName()))
               {
                  // only for strings
                  continue;
               }
               sb.append(separator);
               sb.append(" lower( ").append(alias).append(".").append(valueField.getName()).append(") ");
               sb.append(" like ");
               sb.append(" :").append(searchKind).append("_").append(valueName).append(" ");
               params.put(searchKind + "_" + valueName, likeParam((String) value).trim().toLowerCase());
               separator = " and ";
               break;
            case "obj":
               sb.append(separator);
               sb.append(alias).append(".").append(valueField.getName());
               sb.append(" = ");
               sb.append(" :").append(searchKind).append("_").append(valueName).append(" ");
               params.put(searchKind + "_" + valueName, value);
               separator = " and ";
               break;
            case "not":
               sb.append(separator);
               sb.append(alias).append(".").append(valueField.getName());
               sb.append(" <> ");
               sb.append(" :").append(searchKind).append("_").append(valueName).append(" ");
               params.put(searchKind + "_" + valueName, value);
               separator = " and ";
               break;
            case "from":
               sb.append(separator);
               sb.append(alias).append(".").append(valueField.getName());
               sb.append(" >= ");
               sb.append(" :").append(searchKind).append("_").append(valueName).append(" ");
               if (valueClass.getCanonicalName().equals(Date.class.getCanonicalName()))
               {
                  params.put(searchKind + "_" + valueName, DateUtils.toBeginOfDay((Date) value));
               }
               else
               {
                  params.put(searchKind + "_" + valueName, value);
               }
               separator = " and ";
               break;
            case "to":
               sb.append(separator);
               sb.append(alias).append(".").append(valueField.getName());
               sb.append(" <= ");
               sb.append(" :").append(searchKind).append("_").append(valueName).append(" ");
               if (valueClass.getCanonicalName().equals(Date.class.getCanonicalName()))
               {
                  params.put(searchKind + "_" + valueName, DateUtils.toEndOfDay((Date) value));
               }
               else
               {
                  params.put(searchKind + "_" + valueName, value);
               }
               separator = " and ";
               break;
            case "nil":
               sb.append(separator);
               sb.append(alias).append(".").append(valueField.getName());
               sb.append(" is null ");
               separator = " and ";
               break;
            case "notNil":
               sb.append(separator);
               sb.append(alias).append(".").append(valueField.getName());
               sb.append(" is not null ");
               separator = " and ";
               break;
            }
         }

      }

      logger.info(sb.toString());
      logger.info(params.toString());
   }

   protected boolean isRelevant(Object value)
   {
      if (value == null)
      {
         return false;
      }
      if (value instanceof String && ((String) value).trim().isEmpty())
      {
         return false;
      }
      if (value instanceof Number && ((Number) value).doubleValue() == 0)
      {
         return false;
      }
      if (value instanceof Boolean && !((Boolean) value).booleanValue())
      {
         return false;
      }
      if (value instanceof Iterable)
      {
         return false;
      }
      if (value instanceof Map)
      {
         return false;
      }
      return true;
   }

   protected Search<T> customRestrictions(Search<T> search, String alias,
            String separator, StringBuffer sb, Map<String, Object> params)
            throws Exception
   {
      return null;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private Class<T> getClassType()
   {
      Class clazz = getClass();
      while (!(clazz.getGenericSuperclass() instanceof ParameterizedType))
      {
         clazz = clazz.getSuperclass();
      }
      ParameterizedType parameterizedType = (ParameterizedType) clazz
               .getGenericSuperclass();
      return (Class<T>) parameterizedType.getActualTypeArguments()[0];
   }

   public List<T> getList(Search<T> search) throws Exception
   {
      List<T> result = null;
      Query res = getRestrictions(search, false);
      if (res == null)
         return result;

      return (List<T>) res.getResultList();
   }



}
