package com.runningmate.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class RunningmateBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunningmateBackendApplication.class, args);
	}

}
