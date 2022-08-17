package com.kaellum.walkmydog.user.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * 
 * @author Raphael Cremasco
 * @source https://www.baeldung.com/javax-validation-method-constraints,
 * https://markoantic.medium.com/conditional-validation-in-java-applications-80a24f06d721
 *
 */
public class CustomValidator implements ConstraintValidator<CustomObjectValidation, Object> {

    private String conditionalProperty;
    private String[] requiredProperties;
    private String message;
    private String[] values;
    //https://stackoverflow.com/questions/19402044/alternate-to-beanutils-getproperty
    private static BeanWrapper wrapper;

    @Override
    public void initialize(CustomObjectValidation constraint) {
        conditionalProperty = constraint.conditionalProperty();
        requiredProperties = constraint.requiredProperties();
        message = constraint.message();
        values = constraint.values();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            wrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
            Object conditionalPropertyValue = wrapper.getPropertyValue(conditionalProperty);
            if (doConditionalValidation(conditionalPropertyValue)) {
                return validateRequiredProperties(object, context);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            return false;
        }
        return true;
    }

    private boolean validateRequiredProperties(Object object, ConstraintValidatorContext context) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        boolean isValid = true;
        for (String property : requiredProperties) {
            Object requiredValue = wrapper.getPropertyValue(property);
            boolean isPresent = requiredValue != null && !ObjectUtils.isEmpty(requiredValue);
            if (!isPresent) {
                isValid = false;
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(property)
                        .addConstraintViolation();
            }
        }
        return isValid;
    }

    private boolean doConditionalValidation(Object actualValue) {
        return Arrays.asList(values).contains(actualValue);
    }
}
