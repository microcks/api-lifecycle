package org.acme.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClient;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class OrderServiceApplication {

	@Bean
	public Executor asyncExecutor() {
		return new ThreadPoolTaskExecutor();
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean("pastryRestClient")
	RestClient pastryRestClient(ApplicationProperties properties) {
		return RestClient.create(properties.pastriesBaseUrl());
	}
}
