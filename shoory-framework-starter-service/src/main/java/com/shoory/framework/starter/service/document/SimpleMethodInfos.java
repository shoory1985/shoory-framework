package com.shoory.framework.starter.service.document;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleMethodInfos {
	private String method = "";
	private String name = "";
	private String description = "";
	private String module = "";
}
