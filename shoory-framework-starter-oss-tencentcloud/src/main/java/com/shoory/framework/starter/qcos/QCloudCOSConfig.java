package com.shoory.framework.starter.qcos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;

@Configuration
public class QCloudCOSConfig {
	@Value("${bucket.secretId}")
	public String secretId;
	@Value("${bucket.secretKey}")
	public String secretKey;
	@Value("${bucket.region}")
	public String region;

	@Bean
	 COSClient getCosClient() {
		// 1 初始化用户身份信息(secretId, secretKey)
		COSCredentials cred = new BasicCOSCredentials(this.secretId, this.secretKey);
		// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		// clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
		ClientConfig clientConfig = new ClientConfig(new Region(this.region));
		// 3 生成cos客户端
		
		return new COSClient(cred, clientConfig);
	}
}
