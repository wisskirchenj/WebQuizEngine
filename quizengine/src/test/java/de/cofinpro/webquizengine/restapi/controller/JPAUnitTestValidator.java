package de.cofinpro.webquizengine.restapi.controller;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Supplier;

public class JPAUnitTestValidator<T> {

    private final Validator validator;
    private final Supplier<? extends T> getValidFunction;

    public JPAUnitTestValidator(Supplier<? extends T> getValidObjectFunction) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.getValidFunction = getValidObjectFunction;
    }

    public Set<ConstraintViolation<T>> validate(String fieldName, Object value) throws Exception {
        T object = getValidFunction.get();
        updateFieldByReflection(object, fieldName, value);

        return validator.validate(object);
    }

    private void updateFieldByReflection(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
