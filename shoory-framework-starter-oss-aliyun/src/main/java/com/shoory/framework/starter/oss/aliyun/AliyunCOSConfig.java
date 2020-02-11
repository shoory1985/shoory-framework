package com.shoory.framework.starter.oss.aliyun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;


@Configuration
@ConditionalOnBean(value = AliyunCOSConfigProperties.class)
public class AliyunCOSConfig {
	
	@Autowired
	private AliyunCOSConfigProperties configProperties;
	@Bean
	public OSS ossClient() {
		// 创建ClientConfiguration实例，按照您的需要修改默认参数。
		ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
		// 开启支持CNAME。CNAME是指将自定义域名绑定到存储空间上。
		conf.setSupportCname(true);
		OSS ossClient = new OSSClientBuilder().build(configProperties.getEndPoint(), configProperties.getAccessKeyId(), configProperties.getAccessKeySecret(),conf);
		return ossClient;
	} 

}
