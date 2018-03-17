package com.example.processorapp;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Data;
import lombok.Getter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.MimeType;

import java.io.IOException;

@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
@Log
public class ProcessorApp {

    @Bean
    @StreamMessageConverter
    public MessageConverter greetingConverter() {
        return new GreetingMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApp.class, args);
    }

    @ServiceActivator(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public GreetingResponseMessage transform(GreetingMessage greetingMessage) {
        log.info("received greeting message" + greetingMessage);
        GreetingResponseMessage response = new GreetingResponseMessage(greetingMessage.value + " response!");
        return response;
    }

    @Data
    static class GreetingMessage {
        private String value;
    }

    static class GreetingMessageConverter extends AbstractMessageConverter {

        private final ObjectMapper objectMapper;

        public GreetingMessageConverter() {
            super(MimeType.valueOf("application/xml"));
            objectMapper = new XmlMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return clazz.equals(GreetingMessage.class);
        }

        @Override
        protected Object convertFromInternal(Message<?> message, Class<?> targetClass, @Nullable Object conversionHint) {
            try {
                return objectMapper.readValue((byte[]) message.getPayload(), GreetingMessage.class);
            } catch (IOException e) {
                return null;
            }
        }
    }

    @Getter
    static class GreetingResponseMessage {

        private String response;

        public GreetingResponseMessage(String response) {
            this.response = response;
        }
    }

}
