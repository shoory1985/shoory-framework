package com.shoory.framework.starter.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface ApiArticles {
	public ApiArticle[] value();
}
