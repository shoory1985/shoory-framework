package com.shoory.framework.starter.utils;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class CredentialUtils {
	
	public long getUserId(String credential) {
		int index = credential.indexOf(":");

		try {
			return Long.parseLong(index >= 0 ? credential.substring(index + 1) : credential);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public String getScene(String credential) {
		int index = credential.indexOf(":");
		return index >= 0 
				? credential.substring(0, index)
				: "";
	}
	public String makeCredential(String scene, long userId) {
		return StringUtils.isNotBlank(scene) 
				? scene + ":" + userId
				: String.valueOf(userId);
	}
}
