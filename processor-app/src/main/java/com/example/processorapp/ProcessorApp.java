package com.example.processorapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;

@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
public class ProcessorApp {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApp.class, args);
    }

    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public String transform(String payload) {
        log.info("Processor received {}", payload);
        return payload + " processed";
    }
}
