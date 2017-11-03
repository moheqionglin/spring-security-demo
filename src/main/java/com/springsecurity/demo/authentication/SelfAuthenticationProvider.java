package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthDetailsInfo;
import com.springsecurity.demo.common.AuthResponse;
import com.springsecurity.demo.common.AuthSource;
import com.springsecurity.demo.common.SecurityUtility;
import com.springsecurity.demo.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author wanli zhou
 * @created 2017-10-24 10:58 PM.
 */
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider{

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecurityUtility securityUtility;

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        AuthResponse authResponse = authService.verifyPasswordFromDB(username, password);
        if(!authResponse.isAuthenticate()){
            throw new BadCredentialsException("username or password is not correct");
        }

        //注意 因为 auth.getCredential 是获取不到所以这里用setDetail
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authResponse.getUser(), authResponse.getToken(), securityUtility.getRoles(authResponse.getUser()));
        auth.setDetails(new AuthDetailsInfo(authResponse.getToken(), null, AuthSource.LOGIN_AUTH_FILTER.toString()));
        return auth;
    }

    @Override
    public final boolean supports(Class<?> authentication) {
        return Authentication.class.isAssignableFrom(authentication);
    }
}
