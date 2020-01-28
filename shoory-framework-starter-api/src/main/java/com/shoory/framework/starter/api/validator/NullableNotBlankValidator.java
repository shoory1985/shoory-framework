package com.shoory.framework.starter.api.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.util.StringUtils;

import com.shoory.framework.starter.api.validator.annotation.NullableNotBlank;

public class NullableNotBlankValidator implements ConstraintValidator<NullableNotBlank, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || value.trim().length() > 0;
	}
}