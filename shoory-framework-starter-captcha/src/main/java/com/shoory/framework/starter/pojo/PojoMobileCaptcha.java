package com.shoory.framework.starter.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class PojoMobileCaptcha implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String countryCode;
	
	private String mobile;
	
	private String captchaCode;
	
	private int checkCount;

	private long expiredTime;
}
