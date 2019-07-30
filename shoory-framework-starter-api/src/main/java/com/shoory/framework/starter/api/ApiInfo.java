package com.shoory.framework.starter.api;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {
	private Class<?> apiClass;
	private Class<?>[] dependentAppClasses;
}