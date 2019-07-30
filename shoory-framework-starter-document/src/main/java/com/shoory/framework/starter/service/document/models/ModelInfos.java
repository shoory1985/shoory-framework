package com.shoory.framework.starter.service.document.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelInfos {
	private String className;
	private FieldInfos[] params;
}
