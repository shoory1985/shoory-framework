package com.shoory.framework.starter.api.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.annotation.ApiHidden;
import com.shoory.framework.starter.api.annotation.ApiName;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiName("语言")
	private String lang;
	
	@ApiHidden
	private String _clientAddress;
	
	@ApiHidden
	@JsonIgnore
	private long _startTime = 0;
}
