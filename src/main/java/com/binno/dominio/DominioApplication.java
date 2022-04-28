package com.binno.dominio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DominioApplication {

	public static void main(String[] args) {
		SpringApplication.run(DominioApplication.class, args);
	}

}
