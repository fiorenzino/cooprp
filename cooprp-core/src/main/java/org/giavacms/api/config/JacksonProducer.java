package org.giavacms.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.annotation.PostConstruct;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.List;

/**
 * Created by fiorenzo on 15/11/16.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonProducer implements ContextResolver<ObjectMapper> {
    private ObjectMapper objectMapper = new ObjectMapper() {
        private static final long serialVersionUID = 1L;

        {
            Hibernate5Module hibernate4Module = new Hibernate5Module();
            hibernate4Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
            //hibernate4Module.configure(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
            registerModule(hibernate4Module);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    };

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}

//public class JacksonProducer implements ContextResolver<ObjectMapper>
//{
//
//   private final ObjectMapper json;
//
//   @PostConstruct
//   public void init()
//   {
//      System.out.println("CI PASSO!!");
//   }
//
//   public JacksonProducer() throws Exception
//   {
//      System.out.println("CI PASSO PER JACKSON");
//      Hibernate5Module hibernate5Module = new Hibernate5Module();
//      hibernate5Module = hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
//      this.json
//               = new ObjectMapper()
//               .findAndRegisterModules()
//               .registerModule(hibernate5Module)
//               .registerModule(new JavaTimeModule())
//               .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//               .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//
//      ;
//
//   }
//
//   @Override
//   public ObjectMapper getContext(Class<?> objectType)
//   {
//      System.out.println("Specific producer");
//      List<Module> modules = json.findModules();
//      for (Module m : modules)
//      {
//         System.out.println("Module: " + m.getModuleName());
//      }
//      return json;
//   }
//
//}
