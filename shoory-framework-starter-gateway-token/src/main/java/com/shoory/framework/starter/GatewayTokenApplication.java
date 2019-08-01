package com.shoory.framework.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.gateway.api.GatewayTokenApi;

@SpringBootApplication
@ComponentScan
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableConfigurationProperties
public class GatewayTokenApplication {
	@Bean
	public ApiInfo apiInfo() {
		return new ApiInfo(GatewayTokenApi.class, new Class<?>[] {});
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayTokenApplication.class, args);
	}
} 