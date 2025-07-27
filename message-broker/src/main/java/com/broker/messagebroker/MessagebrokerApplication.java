package com.broker.messagebroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.log4j.Log4j2;

@EntityScan(value = "com.broker")
@ComponentScan(value = "com.broker")
@EnableTransactionManagement
@EnableJpaRepositories(value = "com.broker")
@Log4j2
@SpringBootApplication
public class MessagebrokerApplication implements CommandLineRunner {

	@Autowired
	private Notifier notifier;

	public static void main(String[] args) {
		SpringApplication.run(MessagebrokerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// log.debug("Inside CommmandLineRunner Before Calling Notifier");
		// notifier.sendMessage("Hello Again", 1L);
		// log.debug("After Calling Notifier");
	}

}
