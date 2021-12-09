package com.cnu.spg.user.dto.response;

import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.dto.UserRoleDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserInfoResponse {
    Set<UserRoleDto> roles = new HashSet<>();

    private String name;

    public UserInfoResponse() {
    }

    public UserInfoResponse(User user) {
        this.name = user.getName();

        user.getRoles().forEach(role -> roles.add(new UserRoleDto(role)));
    }
}
