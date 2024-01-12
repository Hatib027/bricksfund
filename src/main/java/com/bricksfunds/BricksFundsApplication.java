package com.bricksfunds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BricksFundsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BricksFundsApplication.class, args);
	}

}
