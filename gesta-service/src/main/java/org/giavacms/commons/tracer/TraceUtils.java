package org.giavacms.commons.tracer;

import io.opentracing.Scope;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.jboss.logging.Logger;

import javax.ejb.EJBContext;
import javax.interceptor.InvocationContext;
import java.util.Map;

public class TraceUtils
{

   private static Logger logger = Logger.getLogger(TraceUtils.class);

   /**
    * Lo scope dev'essere inserito in un'Autoclosable oppure essere chiuso a mano invocando close();
    * Lo scope e' un wrap dello Span (l'oggetto che logga la durata di un evento). Per ottenere lo Span basta invocare
    * scope.span()
    */
   public static Scope startStatic(String className, String methodName)
   {
      try
      {
         SpanContext spanContext = (SpanContext) ThreadLocalUtil.getThreadVariable(TracerInterceptor.SPAN_CONTEXT);
         Scope scope = initScope(className, methodName, spanContext);
         return scope;
      }
      catch (Throwable t)
      {
         logger.warn("Cannot start static method tracing");
         t.printStackTrace();
         return null;
      }
   }

   public static Scope startEJB(EJBContext ejbContext, String className, String methodName)
   {
      try
      {
         SpanContext spanContext = getSpanContextFromEjb(ejbContext.getContextData());
         Scope scope = initScope(className, methodName, spanContext);
         return scope;
      }
      catch (Throwable t)
      {
         logger.warn("Cannot start EJB method tracing");
         t.printStackTrace();
         return null;
      }
   }

   /**
    * Scope e' autocolosable, e bisognerebbe sfruttare tale costrutto, metto qua il metodo end per chiarezza
    */
   public static void end(Scope scope)
   {
      try
      {
         scope.close();
      }
      catch (Throwable t)
      {
         logger.warn("Cannot close scope");
         t.printStackTrace();
      }

   }

   /**
    * Inizializza il ThreadLocal, in preparazione a delle chiamate statiche sfruttando il thread che le esegue
    * (deve seguire una chiamata a startStatic all'inizio del metodo che si desidera misurare)
    */
   public static void init(EJBContext ctx)
   {
      try
      {
         SpanContext spanContext = getSpanContextFromEjb(ctx.getContextData());
         if (spanContext != null)
         {
            ThreadLocalUtil.setThreadVariable(TracerInterceptor.SPAN_CONTEXT, spanContext);
         }
      }
      catch (Throwable t)
      {
         logger.warn(" Cannot init ThreadLocal");
         t.printStackTrace();
         ThreadLocalUtil.destroy();
      }

   }

   public static void init(InvocationContext ctx)
   {
      try
      {
         SpanContext spanContext = getSpanContextFromEjb(ctx.getContextData());
         if (spanContext != null)
         {
            ThreadLocalUtil.setThreadVariable(TracerInterceptor.SPAN_CONTEXT, spanContext);
         }
      }
      catch (Throwable t)
      {
         logger.warn(" Cannot init ThreadLocal");
         t.printStackTrace();
         ThreadLocalUtil.destroy();
      }

   }

   public static void close()
   {
      ThreadLocalUtil.destroy();
   }

   private static Scope initScope(String className, String methodName, SpanContext spanContext)
   {
      Scope scope = null;
      String tag = className + "." + methodName;
      if (GlobalTracer.isRegistered())
      {
         Tracer tracer = GlobalTracer.get();
         Tracer.SpanBuilder spanBuilder = tracer.buildSpan(tag).asChildOf(spanContext);
         spanBuilder.withTag(Tags.COMPONENT.getKey(), tag)
                  .withTag("classname", className)
                  .withTag("method", methodName);
         scope = spanBuilder.startActive();
      }
      return scope;
   }

   private static SpanContext getSpanContextFromEjb(Map<String, Object> ctxData)
   {
      Object variable = ctxData.get(TracerInterceptor.SPAN_CONTEXT);
      if (variable instanceof SpanContext)
      {
         SpanContext spanContext = (SpanContext) variable;
         return spanContext;
      }
      return null;
   }
}
