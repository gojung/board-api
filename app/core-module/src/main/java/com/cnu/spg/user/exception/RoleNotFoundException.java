package com.cnu.spg.user.exception;

import com.cnu.spg.comon.exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    private static final String ROLE_NOT_FOUND_EXCEPTION_MSG = "권한 정보를 찾을 수 없습니다.";

    public RoleNotFoundException() {
        super(ROLE_NOT_FOUND_EXCEPTION_MSG);
    }

    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
