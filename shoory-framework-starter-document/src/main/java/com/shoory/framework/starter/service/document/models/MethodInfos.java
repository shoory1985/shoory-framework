package com.shoory.framework.starter.service.document.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MethodInfos {
	private String method = "";
	private String name = "";
	private String description = "";
	private String module = "默认";
	
	private FieldInfos[] requestFields;
	private FieldInfos[] responseFields;
	private ReturnInfos[] returns;
}
