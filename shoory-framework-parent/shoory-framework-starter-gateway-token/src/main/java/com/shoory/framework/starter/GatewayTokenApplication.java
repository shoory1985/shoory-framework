package com.shoory.framework.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ComponentScan
@EnableDiscoveryClient
public class GatewayTokenApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayTokenApplication.class, args);
	}
} 