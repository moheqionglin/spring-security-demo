package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthCookieUtil;
import com.springsecurity.demo.common.AuthDetailsInfo;
import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wanli zhou
 * @created 2017-10-29 9:54 PM.
 */
public class SelfLogoutSuccessHandler implements LogoutSuccessHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthService authService;
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AuthCookieUtil.removeAuthCookie(httpServletResponse);
        if(authentication != null && authentication.getPrincipal() instanceof UserInfo){
            UserInfo user = (UserInfo) authentication.getPrincipal();
            if(authentication.getDetails() instanceof AuthDetailsInfo){
                AuthDetailsInfo authDetail = (AuthDetailsInfo) authentication.getDetails();
                authService.removeToken(user, authDetail.getToken());
            }

        }

        if(httpServletRequest.getHeader("Accept") != null && httpServletRequest.getHeader("Accept").toLowerCase().contains("application/json")){
            log.debug("json logout request, not sending redirect");
        }else{
            log.debug("sending redirect index.html");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
        }
    }
}
