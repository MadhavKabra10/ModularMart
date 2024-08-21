package com.madhav.ecommerce;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NotificationApplication {
	@Value("${spring.mail.port}")
	int port;
	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}
	@PostConstruct
	public void printMailPort() {
		// This method will run after the application has started
		System.out.println("Mail server is configured to run on port: " + port);
	}
}
