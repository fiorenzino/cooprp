package org.giavacms.commons.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.annotation.PostConstruct;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by fiorenzo on 15/11/16.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonProducer implements ContextResolver<ObjectMapper> {

    private final ObjectMapper json;

    @PostConstruct
    public void init() {
        System.out.println("CI PASSO!!");
    }

    public JacksonProducer() throws Exception {
        System.out.println("CI PASSO PER JACKSON");
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);

        // Necessario per far si che vengano mantenute 2 cifre dopo la virgola anche in caso di zeri
        // Es. 2.00
        SimpleModule bigDecimalModule = new SimpleModule();
        bigDecimalModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        });
        bigDecimalModule.addDeserializer(
                BigDecimal.class, new JsonDeserializer<BigDecimal>() {
                    @Override
                    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                        String stringValue = jp.readValueAs(String.class);
                        BigDecimal value = new BigDecimal(stringValue) ;
                        value.setScale(2, BigDecimal.ROUND_HALF_UP);
                        return value;
                    }
                }
        );

        this.json
                = new ObjectMapper()
                .findAndRegisterModules()
                .registerModule(hibernate5Module)
                .registerModule(new JavaTimeModule())
                .registerModule(bigDecimalModule)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        System.out.println("Specific producer");
        List<Module> modules = json.findModules();
        for (Module m : modules) {
            System.out.println("Module: " + m.getModuleName());
        }
        return json;
    }

}
