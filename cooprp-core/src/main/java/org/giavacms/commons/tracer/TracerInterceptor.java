package org.giavacms.commons.tracer;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.jboss.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.time.Instant;
import java.util.Date;

public class TracerInterceptor
{

   public static final String SPAN_CONTEXT = "__opentracing_span_context";
   private final Logger log = Logger.getLogger(getClass().getName());

   String component;
   String method;

   String tag;

   public String getComponent()
   {
      return tag;
   }

   @AroundInvoke
   public Object wrap(InvocationContext ctx) throws Exception
   {
      if (!GlobalTracer.isRegistered())
      {
         log.debug("GlobalTracer is not registered. Skipping.");
         return ctx.proceed();
      }


      try
      {
         component = ctx.getTarget().getClass().getSimpleName();
         method = ctx.getMethod().getName();
         this.tag = component + "." + method;


         Tracer tracer = GlobalTracer.get();
         Tracer.SpanBuilder spanBuilder = tracer.buildSpan(getComponent());
         spanBuilder.withTag(Tags.COMPONENT.getKey(), getComponent())
                  .withTag("classname", component)
                  .withTag("method", method);

         int contextParameterIndex = -1;
         for (int i = 0; i < ctx.getParameters().length; i++)
         {
            Object parameter = ctx.getParameters()[i];
            if (parameter instanceof SpanContext)
            {
               log.debug("Found parameter as span context. Using it as the parent of this new span");
               spanBuilder.asChildOf((SpanContext) parameter);
               contextParameterIndex = i;
               break;
            }
            else if (parameter instanceof Span)
            {
               log.debug("Found parameter as span. Using it as the parent of this new span");
               spanBuilder.asChildOf((Span) parameter);
               contextParameterIndex = i;
               break;
            }
            else if (parameter != null)
            {
               if (parameter.getClass().isPrimitive() || parameter instanceof Number || parameter instanceof String
                        || parameter instanceof Date || parameter instanceof Instant || parameter.getClass().isEnum())
               {
                  spanBuilder.withTag("param", parameter.toString());
               }
            }

         }

         if (contextParameterIndex < 0)
         {
            log.debug("No parent found. Trying to get span context from context data");
            Object ctxParentSpan = ctx.getContextData().get(SPAN_CONTEXT);
            if (ctxParentSpan != null && ctxParentSpan instanceof SpanContext)
            {
               log.debug("Found span context from context data.");
               SpanContext parentSpan = (SpanContext) ctxParentSpan;
               spanBuilder.asChildOf(parentSpan);
            }
         }

         try (Scope scope = spanBuilder.startActive())
         {
            log.debug("Adding span context into the invocation context.");
            ctx.getContextData().put(SPAN_CONTEXT, scope.span().context());
            TraceUtils.init(ctx);
            if (contextParameterIndex >= 0)
            {
               log.debug("Overriding the original span context with our new context.");
               ctx.getParameters()[contextParameterIndex] = scope.span().context();
            }

            // 2018 08 14 - le eccezioni degli ejb vengono catchate dal tracer se lo faccio qui!
            // return ctx.proceed();
            // lo faccio alla very end
         }
         finally
         {
            TraceUtils.close();
         }
      }
      catch (Throwable throwable)
      {
         log.error(throwable.getMessage(), throwable);
      }

      return ctx.proceed();
   }

}

