package org.giavacms.commons.tracer;

public class ThreadLocalUtil
{

   private final static ThreadLocal<ThreadVariables> THREAD_VARIABLES = new ThreadLocal<ThreadVariables>()
   {

      /**
       * @see ThreadLocal#initialValue()
       */
      @Override
      protected ThreadVariables initialValue()
      {
         return new ThreadVariables();
      }
   };

   public static Object getThreadVariable(String name)
   {
      return THREAD_VARIABLES.get().get(name);
   }

   public static Object getThreadVariable(String name, InitialValue initialValue)
   {
      Object o = THREAD_VARIABLES.get().get(name);
      if (o == null)
      {
         THREAD_VARIABLES.get().put(name, initialValue.create());
         return getThreadVariable(name);
      }
      else
      {
         return o;
      }
   }

   public static void setThreadVariable(String name, Object value)
   {
      THREAD_VARIABLES.get().put(name, value);
   }

   public static void destroy()
   {
      THREAD_VARIABLES.remove();
   }
}

