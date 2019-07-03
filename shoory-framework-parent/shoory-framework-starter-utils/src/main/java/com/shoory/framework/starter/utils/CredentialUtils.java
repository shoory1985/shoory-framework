package com.shoory.framework.starter.utils;

import org.springframework.stereotype.Component;

@Component
public class CredentialUtils {
	
	public long getUserId(String credential) {
		
		try {
			return Long.parseLong(credential);
			
		} catch (NumberFormatException e) {
			return 0;
		}
		
	}

}
