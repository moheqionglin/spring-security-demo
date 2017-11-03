package com.springsecurity.demo.authentication;

import com.springsecurity.demo.common.AuthDetailsInfo;
import com.springsecurity.demo.common.AuthResponse;
import com.springsecurity.demo.common.AuthSource;
import com.springsecurity.demo.common.SecurityUtility;
import com.springsecurity.demo.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Component;


/**
 * @author wanli zhou
 * @created 2017-10-29 10:20 PM.
 */
@Component
public class PreAuthUserAuthenticationManager implements AuthenticationManager {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthService authService;
    @Autowired
    private SecurityUtility securityUtility;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String ip = (String) authentication.getPrincipal();
        String token = (String) authentication.getCredentials();
        AuthResponse authResponse = authService.preAuthVerify(ip, token);
        try {
            if (authResponse.getUser() != null) {
                log.debug("Pre auth token validation successful.");
                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authResponse.getUser(), token, securityUtility.getRoles(authResponse.getUser()));
                authToken.setDetails(new AuthDetailsInfo(token, ip, AuthSource.PRE_AUTH_FILTER.toString()));
                return authToken;
            } else {
                throw new RememberMeAuthenticationException("Could not validate pre auth token.");
            }
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Error trying to validation pre-auth token.", ex);
        }
    }


}
