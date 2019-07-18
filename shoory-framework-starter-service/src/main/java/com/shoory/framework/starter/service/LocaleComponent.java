package com.shoory.framework.starter.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
public class LocaleComponent {
	private Map<String, String> messages = new HashMap<String, String>();

	public LocaleComponent() {
		this.getMessages("i18n");
	}

	public String getMessage(String code, String lang) {
		return messages.get(lang + ":" + code);
	}

	public static String[] split(String s) {
		return s.split("=", 2);
	}

	public void getMessages(String i18nDirectory) {
        String classResourceName = this.getClass().getName().replace(".", "/") + ".class";
        URL classResourceURL = this.getClass().getClassLoader().getResource(classResourceName);
        String classResourcePath = classResourceURL.getPath();

        if (classResourceURL.getProtocol().equals("file")) {
            // 开发环境里class和resource同位于target/classes目录下
            String classesDirPath = classResourcePath.substring(classResourcePath.indexOf("/") + 1, classResourcePath
                    .indexOf(classResourceName));
            File classesDir = new File(classesDirPath + "/" + i18nDirectory);

            enumerateDir(classesDir).stream()
            	.forEach(file -> {
            		String[] pieces = file.getName().replace(".properties", "").split("_");
            		
            		if (pieces.length >= 2) {
            			String lang = pieces[pieces.length - 2] + "_" + pieces[pieces.length - 1];
            			
            			try {
							FileUtils.readLines(file)
								.stream()
								.filter(line -> line.trim().indexOf("=") > 0)
								.map(LocaleComponent::split)
								.filter(array -> StringUtils.isNotBlank(array[0]))
								.forEach(array -> {
									this.messages.put(lang + ":" + array[0].trim(), array[1].trim());
								});
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}		
            	});
            	
            	
        } else if (classResourceURL.getProtocol().equals("jar")) {

            // 打包成jar包时,class和resource同位于jar包里
            String jarPath = classResourcePath.substring(classResourcePath.indexOf("/"), classResourceURL.getPath()
                    .indexOf("!"));

            try {
                JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> jarEntries = jarFile.entries();

                while (jarEntries.hasMoreElements()) {

                    JarEntry jarEntry = jarEntries.nextElement();
                    String resourceName = jarEntry.getName();
                    if (resourceName.indexOf(i18nDirectory + "/") == 0 && !jarEntry.isDirectory()) {
                        //this.getClass().getResourceAsStream(name)
                    }

                }
                jarFile.close();

            } catch (Throwable e) {
                // ignore
            } 
        }
    }

	public static List<File> enumerateDir(File dir) {
		List<File> fileList = new ArrayList<File>();
		if (dir == null) {

		} else if (dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			for (File subFile : subFiles) {
				fileList.add(subFile);
				if (subFile.isDirectory()) {
					fileList.addAll(enumerateDir(subFile));
				}
			}
		} else {
			fileList.add(dir);
		}

		return fileList;
	}
}
