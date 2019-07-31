package com.shoory.framework.starter.service.document.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldInfos {
	private String field = "";
	private String name = "";
	private boolean required = false;
	private String description = "";
	private String examples = "";
	private String className = "";
	private FieldInfos[] params;
}
