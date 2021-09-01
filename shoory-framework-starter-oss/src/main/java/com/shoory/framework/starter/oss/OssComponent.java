package com.shoory.framework.starter.oss;

import java.io.InputStream;
import java.util.List;

public interface OssComponent {
	public String upload(String resourcePath, String mimeType, InputStream is);
	public InputStream download(String resourcePath);
	public void delete(String resourcePath);
	public boolean isExisted(String resourcePath);
	public List<String> list(String path);
}
