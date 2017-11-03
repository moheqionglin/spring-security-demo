package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthCookieUtil;
import com.springsecurity.demo.common.ClientIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class PreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ClientIpFinder ipFinder;

    @Autowired
    public PreAuthenticatedProcessingFilter(PreAuthUserAuthenticationManager authManager){
        setAuthenticationManager(authManager);
    }


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        if(getPreAuthenticatedCredentials(httpServletRequest) == null){
            return null;
        }
        String ip = ipFinder.findIp(httpServletRequest);
        log.debug("Pre-auth ip: {}", ip);
        return ip;
    }

    /**
     * AuthToken
     * */
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() != null) {
            for (Cookie ck : httpServletRequest.getCookies()) {
                if (ck.getName().equalsIgnoreCase(AuthCookieUtil.NAME)) {
                    return ck.getValue();
                }
            }
        }
        return null;
    }
}
