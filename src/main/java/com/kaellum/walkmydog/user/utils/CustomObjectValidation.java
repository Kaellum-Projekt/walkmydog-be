package com.kaellum.walkmydog.user.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * @author Raphael Cremasco
 *
 */

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CustomValidator.class})
@Repeatable(CustomObjectValidation.CustomObjectValidations.class)
public @interface CustomObjectValidation {
	
    String message() default "Field is required.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String conditionalProperty();

    String[] values();

    String[] requiredProperties();   
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Documented 
    @interface CustomObjectValidations {
    	CustomObjectValidation[] value();
    }

}


