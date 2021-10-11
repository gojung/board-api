package com.cnu.spg.user.exception;

public class UserParamterOmittedException extends RuntimeException {
    private static final String USER_PARAMETER_OMITTED_EXCEPTION = "파라미터 값이 누락이 되었습니다.";

    public UserParamterOmittedException() {
        super(USER_PARAMETER_OMITTED_EXCEPTION);
    }
}
