package com.bank.account;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication {
	private static final Logger logger = LoggerFactory.getLogger(AccountApplication.class.getName());
	private static final String separator = " ############################################################################################# ";

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
		logger.info(separator);
		logger.info(" has started successfully.");
		logger.info(separator);
	}

	@PreDestroy
	public void onExit() {
		logger.info(separator);
		logger.info("is shutting down.");
		logger.info(separator);
	}
}