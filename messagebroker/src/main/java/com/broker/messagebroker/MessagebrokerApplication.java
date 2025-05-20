package com.broker.messagebroker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.broker.messageProcessor.SimpleProcessor;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class MessagebrokerApplication implements CommandLineRunner {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private Map<RedisQueueReader, Thread> queueReaders;

	public static void main(String[] args) {
		SpringApplication.run(MessagebrokerApplication.class, args);
	}

	@Override
	public void run(String... args) {

		RedisQueueReader queueReader = new RedisQueueReader("test", stringRedisTemplate, new SimpleProcessor(),
				"TestReader");
		Thread queueReaderThread = new Thread(queueReader);
		this.queueReaders = new HashMap<>();
		this.queueReaders.put(queueReader, queueReaderThread);
		this.queueReaders.forEach((reader, thread) -> thread.start());

	}

	@PreDestroy
	public void closeAllReaders() {
		System.err.println("Inside shutting down in Main");
		queueReaders.forEach((reader, thread) -> reader.shutdownReader());
		queueReaders.forEach((reader, thread) -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Inside Interrupted exception in main");
			}
		});
		System.err.println("After shutting down in Main");
	}

}
