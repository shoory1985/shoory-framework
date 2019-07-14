package com.shoory.framework.starter.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfo {
	private Class<?> apiClass;
	private Class<?>[] dependentAppClasses;
}
