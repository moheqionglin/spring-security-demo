package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthCookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wanli zhou
 * @created 2017-10-24 11:13 PM.
 */
public class SelfAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest hsr, HttpServletResponse response, AuthenticationException a) throws IOException, ServletException {
        log.debug("Authentication failed.");
        if(a instanceof BadCredentialsException){
            log.debug("invalid credentials");
        }
        AuthCookieUtil.removeAuthCookie(response);
        super.onAuthenticationFailure(hsr, response, a);
    }
}
