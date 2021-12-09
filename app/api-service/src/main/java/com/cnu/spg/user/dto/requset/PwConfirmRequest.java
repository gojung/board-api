package com.cnu.spg.user.dto.requset;

import lombok.Data;

import javax.validation.constraints.Pattern;

import static com.cnu.spg.security.util.UserdataValidatorUtils.PASSWORD_REGEX;

@Data
public class PwConfirmRequest {
    @Pattern(regexp = PASSWORD_REGEX)
    private String password;
}
