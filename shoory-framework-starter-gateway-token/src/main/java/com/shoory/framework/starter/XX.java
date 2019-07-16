package com.shoory.framework.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class XX {
	@Value("${spring.cloud.nacos.config.server-addr}")
	private String configServer;

	@Value("${spring.cloud.nacos.discovery.server-addr}")
	private String discoveryServer;

	@Value("${spring.profiles.active:}")
	private String active;

	@Value("${server.port:}")
	private int serverPort;
	
	public void log() {
		System.out.println("==========================");
		System.out.println("configServer="+configServer);
		System.out.println("discoveryServer="+discoveryServer);
		System.out.println("active="+active);
		System.out.println("serverPort="+serverPort);
	}
}
