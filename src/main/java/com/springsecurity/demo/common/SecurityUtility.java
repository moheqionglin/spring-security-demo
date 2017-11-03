package com.springsecurity.demo.common;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wanli zhou
 * @created 2017-10-29 6:11 PM.
 */
@Component
public class SecurityUtility {

    public Collection<? extends GrantedAuthority> getRoles(UserInfo user) {
        List<GrantedAuthority> auths = new ArrayList<>();
        for (RoleInfo role : user.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return auths;
    }
}
