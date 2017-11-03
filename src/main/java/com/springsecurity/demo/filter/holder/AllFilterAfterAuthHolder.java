package com.springsecurity.demo.filter.holder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wanli zhou
 * @created 2017-10-29 10:05 AM.
 */
@Component
@Scope("request")
public class AllFilterAfterAuthHolder {
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean hasToken(){
        return !StringUtils.isEmpty(token);
    }

}
