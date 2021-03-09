package com.shoory.framework.starter.qcos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
public class MinioConfig {
	@Value("${bucket.secretId}")
	public String secretId;
	@Value("${bucket.secretKey}")
	public String secretKey;
	@Value("${bucket.region}")
	public String region;
	

	@Bean
	MinioClient getMinioClient() {
		try {
			return new MinioClient(this.region, 9000,
					this.secretKey,
					this.secretId);
		} catch (InvalidEndpointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
