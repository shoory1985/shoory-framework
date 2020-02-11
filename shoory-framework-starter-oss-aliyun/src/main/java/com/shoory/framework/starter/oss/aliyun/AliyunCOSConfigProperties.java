package com.shoory.framework.starter.oss.aliyun;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss") 
@ConditionalOnProperty(prefix = "aliyun.oss",name = {"accessKeyId","accessKeySecret","endPoint","bucketName"},matchIfMissing = false )
public class AliyunCOSConfigProperties {
	
	private String accessKeyId;
	
	private String accessKeySecret;
	
	private String endPoint;
	
	private String bucketName;
	
}
