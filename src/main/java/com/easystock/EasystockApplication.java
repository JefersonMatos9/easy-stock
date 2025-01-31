package com.easystock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.easystock.model")
public class EasystockApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasystockApplication.class, args);
	}
}
