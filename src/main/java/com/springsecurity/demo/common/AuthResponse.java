package com.springsecurity.demo.common;

/**
 * @author wanli zhou
 * @created 2017-10-29 6:06 PM.
 */
public class AuthResponse {
    private UserInfo user;
    private String token;
    private boolean authenticate;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }
}
