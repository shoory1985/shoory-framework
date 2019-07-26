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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
public class I18nComponent {
	private Hashtable<String, Properties> messages = new Hashtable<String, Properties>();

	public I18nComponent() {
		this.loadResources();
	}

	public String getMessage(String code, String lang) {
		Properties prop = messages.get(lang);
		return prop != null ? prop.getProperty(code) : null;
	}

	private void loadResources() {
		Properties propIndex = loadProperty("i18n/index.properties");
		propIndex.forEach((filename, lang) -> {
			Properties propLang = loadProperty("i18n/"+lang+".properties");
			messages.put(lang.toString(), propLang);
		});
    }
	
	private Properties loadProperty(String resourcePath) {
		Properties prop = new Properties();
		
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
			prop.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prop;
	}

	public Hashtable<String, Properties> getMessages() {
		return messages;
	}

}
