package com.cnu.spg.user.exception;

public class UserTypeIsNotValid extends RuntimeException {
    private static final String USER_TYPE_ERROR_MSG = "is not valid type";

    public UserTypeIsNotValid(String className) {
        super(className + " " + USER_TYPE_ERROR_MSG);
    }
}
