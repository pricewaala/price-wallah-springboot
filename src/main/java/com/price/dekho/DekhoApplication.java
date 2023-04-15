package com.price.dekho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.price.dekho")
public class DekhoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DekhoApplication.class, args);
	}

}
