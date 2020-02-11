package com.shoory.framework.starter.oss.aliyun;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.shoory.framework.starter.oss.OssComponent;

@Component
@ConditionalOnBean(value = AliyunCOSConfig.class)
public class AliyunCOSComponent implements OssComponent {
	@Autowired 
    private AliyunCOSConfigProperties configProperties;
	@Autowired
	private OSS ossClient;

	@Override
	public String upload(String resourcePath, String mimeType, InputStream is) {
		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(configProperties.getBucketName(),resourcePath , is);
			ObjectMetadata om = new ObjectMetadata();
			om.setContentType(mimeType);
			om.setContentLength(is.available());
			PutObjectResult result = ossClient.putObject(putObjectRequest);
			return resourcePath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public InputStream download(String resourcePath) {
		return ossClient.getObject(new GetObjectRequest(configProperties.getBucketName(), resourcePath)).getObjectContent(); 
	}

	@Override
	public void delete(String resourcePath) {
		ossClient.deleteObject(configProperties.getBucketName(), resourcePath);
	}
}
