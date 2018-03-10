package com.example.processorapp;

import lombok.Data;
import lombok.Getter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;

@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
@Log
public class ProcessorApp {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApp.class, args);
    }

    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public GreetingResponseMessage transform(GreetingMessage greetingMessage) {
        log.info("received greeting message" + greetingMessage);
        GreetingResponseMessage response = new GreetingResponseMessage(greetingMessage.greeting);
        return response;
    }

    @Data
    static class GreetingMessage {
        private String greeting;
    }

    @Getter
    static class GreetingResponseMessage {

        private String response;

        public GreetingResponseMessage(String response) {
            this.response = response;
        }
    }

}
