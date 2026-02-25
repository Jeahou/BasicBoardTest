package com.dk.springbootpj1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringbootPj1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootPj1Application.class, args);
	}

}
