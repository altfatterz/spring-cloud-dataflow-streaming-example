package com.example.processorapp;

import lombok.Getter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
@Log
public class ProcessorApp {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApp.class, args);
    }

    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public GreetingMessage transform(Message<GreetingMessage> greetingMessage) {
        log.info("received greeting message" + greetingMessage);
        GreetingMessage response = new GreetingMessage("Hoi");
        return response;
    }

    @Getter
    static class GreetingMessage {

        private String greeting;

        public GreetingMessage(String greeting) {
            this.greeting = greeting;
        }
    }

}
