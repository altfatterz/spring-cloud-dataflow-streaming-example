package com.example.processorapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessorAppTests {

	@Autowired
	private Processor processor;

	@Autowired
	private MessageCollector collector;

	@Test
	public void testMessages() {

		SubscribableChannel input = this.processor.input();

		String msg = "<greeting><value>hello world</value><datetime>2018-03-11T13:49:07</datetime></greeting>";

		input.send(new GenericMessage<>(msg.getBytes(StandardCharsets.UTF_8)));

		BlockingQueue<Message<?>> messages = this.collector.forChannel(processor.output());

		assertThat(messages, receivesPayloadThat(is("{\"response\":\"hello world response!\"}")));
	}

}
