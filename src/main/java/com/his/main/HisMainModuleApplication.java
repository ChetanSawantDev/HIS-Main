package com.his.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
@ComponentScan(basePackages = {
		"com.his.main",
		"org.his.core.kafka.services"
})
public class HisMainModuleApplication {
	public static void main(String[] args) {
		SpringApplication.run(HisMainModuleApplication.class, args);
	}
}
