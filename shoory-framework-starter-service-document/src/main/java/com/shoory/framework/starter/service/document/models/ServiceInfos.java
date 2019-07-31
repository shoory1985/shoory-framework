package com.shoory.framework.starter.service.document.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceInfos {
	private String service;
	private String name;
	private String description;	

	private int methodCount;
	private int modelCount;
	private int moduleCount;
	private int messageCount;
	private int languageCount;
	private List<ServiceInfos> dependentServices = new ArrayList<ServiceInfos>();
}
