package com.shoory.framework.starter.qcos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AliyunCOSConfig {
	@Value("${bucket.secretId}")
	public String secretId;
	@Value("${bucket.secretKey}")
	public String secretKey;
	@Value("${bucket.region}")
	public String region;

}
