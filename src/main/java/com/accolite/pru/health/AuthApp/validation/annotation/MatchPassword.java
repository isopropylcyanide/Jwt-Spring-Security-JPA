package com.accolite.pru.health.AuthApp.validation.annotation;

import com.accolite.pru.health.AuthApp.validation.validator.MatchPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchPasswordValidator.class)
@Documented
public @interface MatchPassword {
	String message() default "The new passwords must match";

	Class<?>[] groups() default {};

	boolean allowNull() default false;

	Class<? extends Payload>[] payload() default {};
}