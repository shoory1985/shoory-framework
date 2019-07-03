package com.shoory.framework.starter.qcos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.shoory.framework.starter.utils.DateUtils;

@Component
public class QCloudCOSComponent {
	@Value("${bucket.name}")
	public String bucketName;

	@Autowired
	private COSClient cosClient;
	@Autowired
	private DateUtils dateUtils;

	public String getExtFileName(String mimeType) {
		switch (mimeType.toLowerCase()) {
		case "text/plain":
			return ".txt";
		case "image/jpeg":
		case "image/jpg":
			return ".jpg";
		case "image/png":
			return ".png";
		case "image/gif":
			return ".gif";
		}
		return "";
	}

	public String upload(String mimeType, InputStream is, long size, long id, String type) {
		ObjectMetadata om = new ObjectMetadata();
		om.setContentType(mimeType);
		om.setContentLength(size);
		
		String path = id + "/" + type + "/" + dateUtils.formatDateTimeSSS(new Date()) + getExtFileName(mimeType);
		PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, path, is, om);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		
		return path;
	}
	
	public String upload(String mimeType, String base64Str, long size, long id, String type) {
		return this.upload(mimeType, new ByteArrayInputStream(Base64.decodeBase64(base64Str)), size, id, type);
	}
	public String upload(String mimeType, byte[] bytes, long size, long id, String type) {
		return this.upload(mimeType, new ByteArrayInputStream(bytes), size, id, type);
	}
	
	public String upload(String mimeType, byte[] bytes, long size,  String path) {
		ObjectMetadata om = new ObjectMetadata();
		om.setContentType(mimeType);
		om.setContentLength(size);
		PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, path, new ByteArrayInputStream(bytes), om);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		return path;
	}

	public byte[] download(String resourcePath) {
		// 指定要下载的文件所在的 bucket 和对象键
		COSObject cosObject = cosClient.getObject(this.bucketName, resourcePath);
		
		
		byte[] result = null;
		
		try {
			InputStream in = cosObject.getObjectContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[40960];
			for (int n; (n = in.read(b)) != -1;) {
				baos.write(b, 0, n);
			}
			result = baos.toByteArray();
			in.close();
		} catch (Exception e) {

		}
		return result;
	}

	public void delete(String resourcePath) {
		// 指定要删除的 bucket 和对象键
		cosClient.deleteObject(bucketName, resourcePath);
	}

}
