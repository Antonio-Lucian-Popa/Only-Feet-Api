package com.asusoftware.only_feet_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OnlyFeetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlyFeetApiApplication.class, args);
	}

}
