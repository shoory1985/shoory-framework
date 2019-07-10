package com.shoory.framework.starter.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {
	@Bean
	public PasswordUtils getPasswordUtils() {
		return new PasswordUtils();
	}
	
	public boolean verify(String md5Password, String savedPassword, String salty) {
		return encode(md5Password, salty).equalsIgnoreCase(savedPassword);
	}
	
	public String encode(String md5Password, String salty) {
		return DigestUtils.md5Hex(md5Password + salty);
	}
}
