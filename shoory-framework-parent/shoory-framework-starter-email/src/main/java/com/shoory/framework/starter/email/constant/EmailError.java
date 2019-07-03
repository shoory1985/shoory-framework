package com.shoory.framework.starter.email.constant;

import com.shoory.framework.starter.api.constants.Resultable;

public enum EmailError implements Resultable {
	ERROR_EMAIL_FAILED("邮件发送失败", ""),
	ERROR_EMAIL_ADDRESS("邮件地址错误", ""),
	ERROR_EMAIL_ATTACHMENT("邮件附件错误", ""),
	;
	private String message;
	private String description;
	
	private EmailError(String message, String description) {
		this.message = message;
		this.description = description;
	}


	@Override
	public String getCode() {
		return null;
	}

	@Override
	public String getMessage(String lang) {
		switch (lang) {
		default:
			return this.message;
		}
	}
	@Override
	public String getDescription(String lang) {
		switch (lang) {
		default:
			return this.description;
		}
	}
}
