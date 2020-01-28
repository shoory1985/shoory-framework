package com.shoory.framework.starter.api.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import com.shoory.framework.starter.api.validator.NullableNotEmptyValidator;

//注解可以作用的位置：字段、方法
@Target({ ElementType.FIELD, ElementType.METHOD })
//运行时注解
@Retention(RetentionPolicy.RUNTIME)
//制定注解判断逻辑所在的类，这个类必须实现了ConstraintValidator接口
@Constraint(validatedBy = NullableNotEmptyValidator.class)
public @interface NullableNotEmpty {
	String message();
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
