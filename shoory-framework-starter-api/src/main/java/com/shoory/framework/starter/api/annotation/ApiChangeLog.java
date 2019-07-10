package com.shoory.framework.starter.api.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(ApiChangeLogs.class)
@Retention(RetentionPolicy.RUNTIME) 
public @interface ApiChangeLog {
	public Class value();
}
