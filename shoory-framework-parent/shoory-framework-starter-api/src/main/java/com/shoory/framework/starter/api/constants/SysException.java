package com.shoory.framework.starter.api.constants;

public class SysException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;

	public SysException(String code, String message) {
		super(code);
		this.code = code;
		this.message = message;
	}

	

	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
