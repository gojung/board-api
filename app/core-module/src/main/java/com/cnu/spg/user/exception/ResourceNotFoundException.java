package com.cnu.spg.user.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(Class<?> cls, Object fieldValue) {
        super(String.format("%s not found with : '%s'", cls, fieldValue));
        this.resourceName = cls.getName();
        this.fieldName = cls.getName();
        this.fieldValue = fieldValue;
    }
}
