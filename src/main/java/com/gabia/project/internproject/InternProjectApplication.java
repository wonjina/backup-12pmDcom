package com.gabia.project.internproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InternProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternProjectApplication.class, args);
	}

}
