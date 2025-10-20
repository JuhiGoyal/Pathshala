package com.pathshala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = {"com.pathshala.repository"})
@ComponentScan(basePackages = {"com.pathshala.*"})
@EntityScan(basePackages = {"com.pathshala.entities"})
@Slf4j
public class OnlineCourseApplication {

	public static void main(String[] args) {
		try {
			log.info("Application Starting...");
			SpringApplication.run(OnlineCourseApplication.class, args);
			log.info("Application Started Successfully.");
		} catch (Exception ex) {
			log.error("Failed to start application" + ex);
		}
	}

}
