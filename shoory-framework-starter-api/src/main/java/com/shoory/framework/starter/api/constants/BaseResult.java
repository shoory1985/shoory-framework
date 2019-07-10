package com.shoory.framework.starter.api.constants;

public enum BaseResult implements Resultable {
	SUCCESS("", ""),
	ERROR_UNKNOWN("未知错误", ""), 
	ERROR_METHOD_NOT_FOUND("方法未找到", ""), 
	ERROR_INTERNAL("内部错误", ""), 
	ERROR_INVALID_PARAMETERS("不合法的参数", ""),
	ERROR_WRONG_PASSWORD("密码错误", ""),
	ERROR_INVALID_TOKEN("非法令牌", ""),
	ERROR_MISSING_TOKEN("令牌未找到", ""),
	;
	private String message;
	private String description;
	
	private BaseResult(String message, String description) {
		this.message = message;
		this.description = description;
	}


	@Override
	public String getCode() {
		return this.toString();
	}

	@Override
	public String getMessage(String lang) {
		if (lang == null) {return "";}
		switch (lang) {
		default:
			return this.message;
		}
	}
	@Override
	public String getDescription(String lang) {
		if (lang == null) {return "";}
		switch (lang) {
		default:
			return this.description;
		}
	}
}
