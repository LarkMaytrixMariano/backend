package com.fpedFIND;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableWebMvc
@EnableCaching
   public class FindFpedApplication {
	public static void main(String[] args) throws Exception{
		SpringApplication.run(FindFpedApplication.class, args);
	}

}






  
  

