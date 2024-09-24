package com.bank.account.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailNotContainsNameValidator.class)
public @interface EmailNotContainsName {
    String message() default "Email must not contain the name";
    boolean ignoreCase() default true;
}