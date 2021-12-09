package com.cnu.spg.user.dto;

import com.cnu.spg.user.domain.Role;
import com.cnu.spg.user.domain.RoleName;
import lombok.Data;

@Data
public class UserRoleDto {
    RoleName roleName;

    public UserRoleDto(Role role) {
        this.roleName = role.getName();
    }
}
