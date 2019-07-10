package com.shoory.framework.starter.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shoory.framework.starter.api.annotation.ApiName;

import lombok.Data;

@Data
public class PojoImageCaptcha implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String captchaId;

	private String imageBase64Str;

	private int remains;

	private long expiredTime;

}
