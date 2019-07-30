package com.shoory.framework.starter.service.document.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleInfos {
	private String module = "";
	private Map<String, SimpleMethodInfos> methods;
}
