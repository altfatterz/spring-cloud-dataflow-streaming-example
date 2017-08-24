package com.example.sourceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;

import java.util.Date;

@EnableBinding(Source.class)
@SpringBootApplication
public class SourceApp {

    public static void main(String[] args) {
        SpringApplication.run(SourceApp.class, args);
    }

    @InboundChannelAdapter(value = Source.OUTPUT)
    public String source() {
        return new Date().toString();
    }
}
