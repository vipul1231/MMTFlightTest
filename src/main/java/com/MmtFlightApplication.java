package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mmt.flights"})
public class MmtFlightApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmtFlightApplication.class, args);
	}

}
