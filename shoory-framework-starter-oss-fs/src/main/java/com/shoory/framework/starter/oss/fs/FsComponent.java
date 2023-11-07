package com.shoory.framework.starter.oss.fs;

import com.shoory.framework.starter.oss.OssComponent;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FsComponent implements OssComponent {
	@Value("${oss.fs.basePath}")
	public String basePath;


	@Override
	public String upload(String resourcePath, String mimeType, InputStream is) {
		try {
			//检查/创建文件夹
			String realPath = this.basePath;
			{
				String[] pieces = resourcePath.split("/");	//切割
				for (int i = 0; i < pieces.length; i++) {
					if (StringUtils.isNotBlank(pieces[i])) {
						realPath += pieces[i];
						if (i < pieces.length - 1) {
							//创建文件夹
							File dir = new File(realPath);
							if (!dir.exists()) {
								dir.mkdir();
								dir.setWritable(true);
								dir.setReadable(true);
								dir.setExecutable(true);
							}
							realPath += "/";
						}
					}
				}
			}

			//写入文件
			FileOutputStream os = new FileOutputStream(realPath);
			byte[] b = new byte[4096];
			int len = 0;
			while ((len = is.read(b)) > 0) {
				os.write(b, 0, len);// 写入数据
			}
			is.close();
			os.flush();
			os.close();

			File file = new File(realPath);
			file.setWritable(true);
			file.setReadable(true);

			return resourcePath;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public InputStream download(String resourcePath) {
		try {
			File file = new File(basePath + resourcePath);

			return file.exists() ? new FileInputStream(file) : null;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(String resourcePath) {
		// 指定要删除的 bucket 和对象键
		try {
			File file = new File(basePath + resourcePath);
			if (file.exists()) {
				file.delete();
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isExisted(String resourcePath) {

		// TODO Auto-generated method stub
		try {
			File file = new File(basePath + resourcePath);
			return file.exists();
		} catch (Throwable e) {
		}
		return false;
	}

	@Override
	public List<String> list(String path) {
		try {
			File file = new File(basePath + path);
			return Arrays.stream(file.list()).collect(Collectors.toList());
		} catch (Throwable e) {
		}
		return new ArrayList<>();
	}
}
