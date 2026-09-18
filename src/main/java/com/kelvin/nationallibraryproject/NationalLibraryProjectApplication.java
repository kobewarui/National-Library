package com.kelvin.nationallibraryproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class NationalLibraryProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NationalLibraryProjectApplication.class, args);
		
		
	}
	 @Bean
	    public ThreadPoolTaskScheduler taskScheduler() {
	        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	        taskScheduler.setPoolSize(5); // You can adjust the pool size as needed.
	        taskScheduler.setThreadNamePrefix("BatteryCheckTask-");
	        return taskScheduler;
	    }
	
}
