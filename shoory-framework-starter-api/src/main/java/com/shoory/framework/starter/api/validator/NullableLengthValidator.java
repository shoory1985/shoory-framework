package com.shoory.framework.starter.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.shoory.framework.starter.api.validator.annotation.NullableLength;

public class NullableLengthValidator implements ConstraintValidator<NullableLength, String> {
	private int min;
	private int max;
    /**
     * 初始化
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(NullableLength constraintAnnotation) {
        //获取禁止的词汇
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }
    
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null ||
				value.length() >= this.min &&
				value.length() <= this.max;
	}


}