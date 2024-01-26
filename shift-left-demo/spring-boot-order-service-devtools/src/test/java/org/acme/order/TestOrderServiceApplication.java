package org.acme.order;

import org.springframework.boot.SpringApplication;

/**
 * A Test instance of the OrderServiceApplication.
 * @author laurent
 */
class TestOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrderServiceApplication::main)
				.with(ContainersConfiguration.class)
				.run(args);
	}
}
