package com.example.sourceapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.MimeType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EnableBinding(Source.class)
@SpringBootApplication
public class SourceApp {

    @Bean
    @StreamMessageConverter
    public MessageConverter greetingConverter() {
        return new GreetingConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(SourceApp.class, args);
    }

    /*
     * poller configured with spring.integration.poller.fixed-delay property (see ChannelBindingAutoConfiguration)
     */
    @InboundChannelAdapter(value = Source.OUTPUT)
    public Greeting source() {
        return new Greeting("hello world", LocalDateTime.of(2018, 3, 11, 13, 49, 7));
    }

    /*
     * headers: contentType: application/json
     * Payload: { "greeting":"hello world" }
     */
    @Getter
    @AllArgsConstructor
    static class Greeting {
        String value;
        LocalDateTime dateTime;
    }

    static class GreetingConverter extends AbstractMessageConverter {

        private final ObjectMapper objectMapper;

        protected GreetingConverter() {
            super(MimeType.valueOf("application/xml"));
            objectMapper = new XmlMapper();
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
            objectMapper.registerModule(javaTimeModule);
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return clazz.equals(Greeting.class);
        }

        @Override
        protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
            try {
                return objectMapper.writer().withRootName("greeting").writeValueAsString(payload)
                        .getBytes(StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

}
