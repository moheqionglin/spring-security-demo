package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthCookieUtil;
import com.springsecurity.demo.common.AuthDetailsInfo;
import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wanli zhou
 * @created 2017-11-11 10:52 PM.
 */
@Component
public class SessionExceedSecurityContextLogoutHandler implements LogoutHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthService authService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        AuthCookieUtil.removeAuthCookie(response);

        Cookie cookie = new Cookie("remember-me", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if(authentication != null && authentication.getPrincipal() instanceof UserInfo){
            UserInfo user = (UserInfo) authentication.getPrincipal();
            AuthDetailsInfo authDetail = (AuthDetailsInfo) authentication.getDetails();
            authService.removeToken(user, authDetail.getToken());
        }
    }


}
