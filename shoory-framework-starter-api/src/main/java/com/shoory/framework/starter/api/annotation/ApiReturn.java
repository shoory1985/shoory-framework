package com.shoory.framework.starter.api.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(ApiReturns.class)
@Retention(RetentionPolicy.RUNTIME) 
public @interface ApiReturn {
	public String code();
	public String message() default "";
	public String description() default "";
}
