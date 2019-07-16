package com.shoory.framework.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import com.shoory.framework.starter.api.ApiInfo;
import com.shoory.framework.starter.gateway.api.GatewayTokenApi;
import com.shoory.framework.starter.service.SpringUtil;

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
		
		XX xx = SpringUtil.getBean(XX.class);
		xx.log();
	}
} 