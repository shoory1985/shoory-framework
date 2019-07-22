package com.shoory.framework.starter.api.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(ApiArticles.class)
@Retention(RetentionPolicy.RUNTIME) 
public @interface ApiArticle {
	public String link();
	public String title() default "";
}
