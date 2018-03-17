package com.example.sourceapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SourceAppTests {

	@Autowired
	private Source source;

	@Autowired
	private MessageCollector collector;

	@Test
	public void testMessages() {
		BlockingQueue<Message<?>> messages = this.collector.forChannel(source.output());

		String msg = "<greeting><value>hello world</value><datetime>2018-03-11T13:49:07</datetime></greeting>";

		assertThat(messages, receivesPayloadThat(is(msg.getBytes(StandardCharsets.UTF_8))));
	}

}
