package com.shoory.framework.starter.qcos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.oss.OssComponent;
import com.shoory.framework.starter.utils.DateUtils;

@Component
public class AliyunCOSComponent implements OssComponent {
	@Value("${bucket.name}")
	public String bucketName;

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

	@Override
	public String upload(String resourcePath, String mimeType, InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream download(String resourcePath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String resourcePath) {
		// TODO Auto-generated method stub
		
	}

}
