package com.holidays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HolidaysServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolidaysServiceApplication.class, args);
	}

}
