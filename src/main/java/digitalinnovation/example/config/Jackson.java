package digitalinnovation.example.config;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import digitalinnovation.example.enums.Raca;



@Configuration
public class Jackson {

    // Qualquer um que chame o ObjectMapper terá as seguintes configurações
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Propriedades não mapeadas não quebram - Ex: tentar transformar um JSON de 5 propriedades em um Object com apenas 3
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Falha se alguma propriedade estiver vazia
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Serve para compatibilidade de arrays, quando tem um array com um item, caso
        // não tenha essa config ele se perde
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        // Serializa datas
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(racaModuleMapper());

        return objectMapper;
    }

    public SimpleModule racaModuleMapper() {
            
        SimpleModule racaModule = new SimpleModule("JSONRacaModule", PackageVersion.VERSION);
        racaModule.addSerializer(Raca.class, new RacaSerialize());
        racaModule.addDeserializer(Raca.class, new RacaDeserialize());

        return racaModule;
    }

    class RacaSerialize extends StdSerializer<Raca> {

        public RacaSerialize() {
            super(Raca.class);
        }

        @Override
        public void serialize(Raca raca, JsonGenerator json, SerializerProvider provider) throws IOException {
            json.writeString(raca.getValue());
        }
    }

    class RacaDeserialize extends StdDeserializer<Raca> {

        public RacaDeserialize() {
            super(Raca.class);
        }

        @Override
        public Raca deserialize(JsonParser p, DeserializationContext ctxt) throws IOException { 
            String value = p.getText();
            return Raca.of(p.getText());
        }

    }

}