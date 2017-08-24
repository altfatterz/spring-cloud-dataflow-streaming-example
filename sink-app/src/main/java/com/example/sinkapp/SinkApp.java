package com.example.sinkapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@SpringBootApplication
@Slf4j
public class SinkApp {

    public static void main(String[] args) {
        SpringApplication.run(SinkApp.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void logger(String payload) {
        log.info("received {}", payload);
    }
}
