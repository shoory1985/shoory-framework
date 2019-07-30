package com.shoory.framework.starter.service.document.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceInfos {
	private String service;
	private String name;
	private String description;	
	
	private SimpleMethodInfos[] methods;
}
