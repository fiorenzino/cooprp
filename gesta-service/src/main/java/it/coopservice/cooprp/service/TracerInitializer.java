package it.coopservice.cooprp.service;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import io.opentracing.util.GlobalTracer;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class TracerInitializer
{
   private static final Logger log = Logger.getLogger(TracerInitializer.class.getName());

   @PostConstruct
   public void init()
   {
      if (GlobalTracer.isRegistered())
      {
         log.info("A Tracer is already registered at the GlobalTracer. Skipping resolution via TraceResolver.");
         return;
      }

      Tracer tracer = TracerResolver.resolveTracer();
      if (null == tracer)
      {
         log.info("Could not get a valid OpenTracing Tracer from the classpath. Skipping.");
         return;
      }

      log.info(String.format("Registering %s as the OpenTracing Tracer", tracer.getClass().getName()));
      GlobalTracer.register(tracer);
   }
}
