package org.giavacms.core.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.giavacms.api.repository.Search;
import org.jboss.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AbstractRestClient<T>
{

   protected Logger logger = Logger.getLogger(getClass());

   protected final String HOST;
   protected final String PATH;
   protected final String ID_FIELD;
   protected final Class<T> CLAZZ;

   private String token;

   public AbstractRestClient(
            final String host,
            final String path,
            final String idField,
            final Class<T> clazz)
   {
      HOST = host;
      PATH = path;
      ID_FIELD = idField.trim();
      CLAZZ = clazz;
   }

   public AbstractRestClient(
            final String path,
            final String idField,
            final Class<T> clazz)
   {
      HOST = getHostName();
      PATH = path;
      ID_FIELD = idField.trim();
      CLAZZ = clazz;
   }

   public AbstractRestClient()
   {
      HOST = getHostName();
      PATH = "";
      ID_FIELD = "";
      CLAZZ = null;
   }

   public List<T> list(
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams,
            Search<T> search) throws Exception
   {
      addFilters(CLAZZ, search, queryParams);
      headerParams = fillHeaders(headerParams);
      return RestStaticClient.list(HOST, PATH, getListType(CLAZZ), queryParams, pathParams, headerParams);
   }

   public List<T> list(int startRow, int pageSize, Search<T> search) throws Exception
   {
      Map<String, Object> queryParams = new HashMap<>();
      queryParams.put("startRow", startRow);
      queryParams.put("pageSize", pageSize);
      Map<String, Object> pathParams = new HashMap<>();
      Map<String, Object> headerParams = new HashMap<>();
      headerParams = fillHeaders(headerParams);
      addFilters(CLAZZ, search, queryParams);
      return list(
               queryParams,
               pathParams,
               headerParams,
               search
      );
   }

   public List<T> list(int startRow, int pageSize) throws Exception
   {
      Map<String, Object> queryParams = new HashMap<>();
      queryParams.put("startRow", startRow);
      queryParams.put("pageSize", pageSize);
      Map<String, Object> pathParams = new HashMap<>();
      Map<String, Object> headerParams = new HashMap<>();
      Search<T> search = new Search<T>(CLAZZ);
      addFilters(CLAZZ, search, queryParams);
      headerParams = fillHeaders(headerParams);
      return list(
               queryParams,
               pathParams,
               headerParams,
               search
      );
   }

   public String listSize(
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams,
            Search<T> search) throws Exception
   {
      addFilters(CLAZZ, search, queryParams);
      headerParams = fillHeaders(headerParams);
      return RestStaticClient.listSize(HOST, PATH, queryParams, pathParams, headerParams);
   }

   public T get(
            Object id,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams) throws Exception
   {
      pathParams.put(ID_FIELD, "" + id);
      headerParams = fillHeaders(headerParams);
      return RestStaticClient.get(HOST,
               PATH + "/{" + ID_FIELD.trim() + "}",
               CLAZZ,
               queryParams,
               pathParams,
               headerParams);
   }

   public T get(Object id) throws Exception
   {

      if (id == null)
      {
         throw new RuntimeException("Invalid uuid!");
      }

      Map<String, Object> queryParams = new HashMap<>();
      Map<String, Object> pathParams = new HashMap<>();
      Map<String, Object> headerParams = new HashMap<>();
      headerParams.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
      headerParams.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
      fillHeaders(headerParams);
      return get(
               id,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T get(
            String resourcePath, Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams) throws Exception
   {
      headerParams.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
      headerParams.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
      fillHeaders(headerParams);
      return RestStaticClient.get(
               HOST,
               PATH + resourcePath,
               CLAZZ,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T post(
            Object entity,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams) throws Exception
   {
      fillHeaders(headerParams);
      return RestStaticClient.post(
               HOST,
               PATH,
               entity,
               CLAZZ,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T post(
            String resourcePath,
            Object entity,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams) throws Exception
   {
      fillHeaders(headerParams);
      return RestStaticClient.post(
               HOST,
               PATH + resourcePath,
               entity,
               CLAZZ,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T post(T entity) throws Exception
   {
      if (entity == null)
      {
         throw new RuntimeException("Invalid entity!");
      }

      Map<String, Object> queryParams = new HashMap<>();
      Map<String, Object> pathParams = new HashMap<>();
      Map<String, Object> headerParams = new HashMap<>();
      headerParams = fillHeaders(headerParams);
      return post(
               entity,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T put(
            Object entity,
            Object id,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams) throws Exception
   {
      pathParams.put(ID_FIELD, id);
      headerParams = fillHeaders(headerParams);
      return RestStaticClient.put(
               HOST,
               PATH + "/{" + ID_FIELD + "}",
               entity,
               CLAZZ,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T put(
            String resourcePath,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> headerParams) throws Exception
   {
      headerParams = fillHeaders(headerParams);
      return RestStaticClient.put(
               HOST,
               PATH + resourcePath,
               CLAZZ.newInstance(), // unused
               CLAZZ,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public T put(Object id, T entity) throws Exception
   {
      if (id == null || entity == null)
      {
         throw new RuntimeException("Invalid uuid or entity!");
      }

      Map<String, Object> queryParams = new HashMap<>();
      Map<String, Object> pathParams = new HashMap<>();
      pathParams.put(ID_FIELD, id);
      Map<String, Object> headerParams = new HashMap<>();
      headerParams.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
      headerParams.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
      headerParams = fillHeaders(headerParams);
      return put(
               entity,
               id,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public void delete(
            String id,
            Map<String, String> queryParams,
            Map<String, String> pathParams,
            Map<String, Object> headerParams) throws Exception
   {

      pathParams.put(ID_FIELD, id);
      headerParams = fillHeaders(headerParams);
      RestStaticClient.delete(
               HOST,
               PATH + "/{" + ID_FIELD + "}",
               queryParams,
               pathParams,
               headerParams
      );
   }

   public void delete(String uuid) throws Exception
   {
      if (uuid == null)
      {
         throw new RuntimeException("Invalid uuid!");
      }
      Map<String, String> queryParams = new HashMap<>();
      Map<String, String> pathParams = new HashMap<>();
      Map<String, Object> headerParams = new HashMap<>();
      pathParams.put("uuid", "" + uuid);
      headerParams = fillHeaders(headerParams);
      delete(
               uuid,
               queryParams,
               pathParams,
               headerParams
      );
   }

   public boolean exits(Long uuid) throws Exception
   {
      Map<String, Object> queryParams = new HashMap<>();
      Map<String, Object> pathParams = new HashMap<>();
      pathParams.put(ID_FIELD, uuid);
      Map<String, Object> headerParams = new HashMap<>();
      headerParams = fillHeaders(headerParams);
      String uuidResult = RestStaticClient.get(
               HOST,
               PATH + "/{ " + ID_FIELD + " }",
               String.class,
               queryParams,
               pathParams,
               headerParams
      );

      return uuidResult != null && !uuidResult.trim().isEmpty();
   }

   private static <T> GenericType<List<T>> getListType(Class<T> clazz)
   {
      ParameterizedType genericType = new ParameterizedType()
      {
         public Type[] getActualTypeArguments()
         {
            return new Type[] { clazz };
         }

         public Type getRawType()
         {
            return List.class;
         }

         public Type getOwnerType()
         {
            return List.class;
         }
      };
      return new GenericType<List<T>>(genericType)
      {
      };
   }

   @Deprecated
   private void _addFilters(Class<T> entity, Search<T> search, Map<String, Object> queryParams)
   {

      List<Field> fieldList = Arrays.asList(entity.getDeclaredFields());

      T obj = search.getObj();
      T like = search.getLike();
      T from = search.getFrom();
      T to = search.getTo();
      T not = search.getNot();
      T nil = search.getNil();
      T notNil = search.getNotNil();

      for (Field field : fieldList)
      {
         field.setAccessible(true);
         Class valueClass = field.getType();

         //Se il campo e' static final non ha senso passarlo.
         boolean isStaticFinal = isStaticFinal(field);
         if (isStaticFinal)
         {
            continue;
         }
         if (valueClass.getAnnotation(Entity.class) != null)
         {
            continue;
         }
         if (field.getAnnotation(Transient.class) != null)
         {
            continue;
         }

         try
         {
            Object value = field.get(obj);
            if (value != null)
            {
               String key = "obj." + field.getName();
               queryParams.put(key, value);
            }
         }
         catch (Exception e)
         {
            System.out.println(e.getMessage());
         }

         try
         {
            Object value = field.get(like);
            if (value != null)
            {
               String key = "like." + field.getName();
               queryParams.put(key, value);
            }
         }
         catch (Exception e)
         {

         }

         try
         {
            Object value = field.get(from);
            if (value != null)
            {
               String key = "from." + field.getName();
               queryParams.put(key, value);
            }
         }
         catch (Exception e)
         {

         }

         try
         {
            Object value = field.get(to);
            if (value != null)
            {
               String key = "to." + field.getName();
               queryParams.put(key, value);
            }
         }
         catch (Exception e)
         {

         }

         try
         {
            Object value = field.get(not);
            if (value != null)
            {
               String key = "not." + field.getName();
               queryParams.put(key, value);
            }
         }
         catch (Exception e)
         {

         }

      }

   }

   private void addFilters(Class<T> entity, Search<T> search, Map<String, Object> queryParams)
   {

      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

      Map<String, Method> methods = new HashMap<>();
      for (Method method : entity.getDeclaredMethods())
      {
         if (method.getName().startsWith("get") || method.getName().startsWith("is"))
         {
            methods.put(method.getName().toLowerCase(), method);
         }
      }

      T filterInstance = null;
      String searchKind = null;
      Field[] searchFields = search.getClass().getDeclaredFields();
      for (Field searchField : searchFields)
      {

         searchKind = searchField.getName();

         switch (searchKind)
         {
         case "obj":
            filterInstance = search.getObj();
            break;
         case "like":
            filterInstance = search.getLike();
            break;
         case "not":
            filterInstance = search.getNot();
            break;
         case "from":
            filterInstance = search.getFrom();
            break;
         case "to":
            filterInstance = search.getTo();
            break;
         case "nil":
            filterInstance = search.getNil();
            break;
         case "notNil":
            filterInstance = search.getNotNil();
            break;
         default:
            continue;
         }

         if (filterInstance == null)
         {
            continue;
         }

         List<Field> entityFields = Arrays.asList(entity.getDeclaredFields());

         for (Field valueField : entityFields)
         {

            valueField.setAccessible(true);
            Class valueClass = valueField.getType();
            String valueName = valueField.getName();

            //Se il campo e' static final non ha senso passarlo.
            boolean isStaticFinal = isStaticFinal(valueField);
            if (isStaticFinal)
            {
               continue;
            }

            if (valueClass.getAnnotation(Entity.class) != null)
            {
               // se il campo è a sua volta una entity dovrei entrarci ricorsivamente....
               boolean todo = true;
               if (todo)
               {
                  continue;
               }
            }
            if (valueField.getAnnotation(Transient.class) != null)
            {
               // i campi jpa transienti mi possono invece servire proprio per comandare ricerche
               // continue;
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

            if (valueField.getAnnotation(JsonIgnore.class) != null)
            {
               // i campi json transienti no invece
               continue;
            }

            // e.g. search.obj.sticazzi_uuid
            try
            {
               Object value = valueField.get(filterInstance);
               if (!isRelevant(value))
               {
                  continue;
               }

               if (value != null)
               {

                  if (value instanceof Date)
                  {
                     value = df.format(value);
                  }
                  else if (value instanceof Calendar)
                  {
                     Date date = ((Calendar) value).getTime();
                     value = df.format(date);
                  }

                  String key = searchKind + "." + valueField.getName();
                  queryParams.put(key, value);
               }
            }
            catch (Exception e)
            {
               System.err.println(e.getMessage());
            }

         }

      }
   }

   protected static boolean isRelevant(Object value)
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

   public static boolean isStaticFinal(Field field)
   {
      int modifiers = field.getModifiers();
      return (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
   }

   abstract protected String getHostName();

   abstract protected Map<String, Object> getHeaders();

   abstract protected Map<String, Object> fillHeaders(Map<String, Object> headers);
}
