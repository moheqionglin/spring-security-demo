package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthCookieUtil;
import com.springsecurity.demo.common.AuthDetailsInfo;
import com.springsecurity.demo.common.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author wanli zhou
 * @created 2017-10-24 11:01 PM.
 */
public class SelfAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RememberMeServices rememberMeServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        rememberMeServices.loginSuccess(httpServletRequest, httpServletResponse, authentication);
        UserInfo user = (UserInfo) authentication.getPrincipal();
        AuthDetailsInfo details = (AuthDetailsInfo) authentication.getDetails();
        addAuthCookie(user, details.getToken(), httpServletResponse, httpServletRequest);
        HashMap<String, String> resp = new HashMap();
        resp.put("email", user.getEmail());
        resp.put("authenticated", details.getToken());
        log.info("[SelfAuthenticationSuccessHandler] success auth {}", details.getToken());
        httpServletResponse.getWriter().write(resp.toString());
        httpServletResponse.getWriter().flush();
    }

    private void addAuthCookie(UserInfo u, String token, HttpServletResponse resp, HttpServletRequest httpServletRequest) {
        final Cookie cookie = new Cookie(AuthCookieUtil.NAME, token);
        cookie.setPath("/");
        cookie.setMaxAge(AuthCookieUtil.TWO_MONTHS_IN_SEC);
        if(!httpServletRequest.getRequestURL().toString().contains("localhost")){
            cookie.setSecure(true); //sending this to any remote host should only be done through https
        }
        cookie.setHttpOnly(true);
        resp.addCookie(cookie);
    }


}
