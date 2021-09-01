package com.shoory.framework.starter.qcos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.shoory.framework.starter.oss.OssComponent;
import com.shoory.framework.starter.utils.DateUtils;

@Component
public class QCloudCOSComponent implements OssComponent {
	@Value("${bucket.name}")
	public String bucketName;

	@Autowired
	private COSClient cosClient;

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

	public String upload(String path, String mimeType, InputStream is) {
		try {
			ObjectMetadata om = new ObjectMetadata();
			om.setContentType(mimeType);
			om.setContentLength(is.available());
			PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, path, is, om);
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return path;
	}

	public InputStream download(String resourcePath) {
		// 指定要下载的文件所在的 bucket 和对象键
		return Optional.ofNullable(cosClient.getObject(this.bucketName, resourcePath))
			.map(COSObject::getObjectContent)
			.orElse(null);
		/*byte[] result = null;
		
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
		*/
	}

	public void delete(String resourcePath) {
		// 指定要删除的 bucket 和对象键
		cosClient.deleteObject(bucketName, resourcePath);
	}

	@Override
	public boolean isExisted(String resourcePath) {
		// TODO Auto-generated method stub
		return cosClient.getObject(this.bucketName, resourcePath) != null;
	}

	@Override
	public List<String> list(String path) {
		return cosClient.listObjects(this.bucketName, path).getObjectSummaries().stream()
				.map(r -> r.getKey())
				.collect(Collectors.toList());
	}

}
