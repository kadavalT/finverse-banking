package com.finverse.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FinverseEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinverseEurekaServerApplication.class, args);
	}

}
