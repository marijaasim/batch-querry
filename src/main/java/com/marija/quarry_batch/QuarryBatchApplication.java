package com.marija.quarry_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuarryBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuarryBatchApplication.class, args);
	}

}
