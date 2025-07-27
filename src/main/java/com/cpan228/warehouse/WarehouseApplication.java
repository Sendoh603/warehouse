package com.cpan228.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WarehouseApplication {

	public static void main(String[] args) {

		SpringApplication.run(WarehouseApplication.class, args);
	}

}
