package com.springsecurity.demo.common;

import com.springsecurity.demo.domain.Role;

/**
 * @author wanli zhou
 * @created 2017-10-29 5:58 PM.
 */
public class RoleInfo {

    public RoleInfo(Role r) {
        this.role = r.getRoleName();
    }

    private String role;

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
                "role='" + role + '\'' +
                '}';
    }

    public void setRole(String role) {
        this.role = role;
    }
}
