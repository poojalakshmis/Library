package com.collabera.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan(basePackages = "com.collabera.library.entity")
@SpringBootApplication(scanBasePackages = "com.collabera.library")

public class LibraryApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
