package com.springsecurity.demo.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author wanli zhou
 * @created 2017-11-11 11:55 PM.
 */
public class OnlyOnePersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {
    private PersistentTokenRepository tokenRepository ;

    public OnlyOnePersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    @Override
    protected void onLoginSuccess(HttpServletRequest request,
                                  HttpServletResponse response, Authentication successfulAuthentication) {
        String username = successfulAuthentication.getName();

        logger.debug("Creating new persistent login for user " + username);

        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(
                username, generateSeriesData(), generateTokenData(), new Date());
        try {
            tokenRepository.removeUserTokens(username);
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        }
        catch (Exception e) {
            logger.error("Failed to save persistent token ", e);
        }
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request,
                           HttpServletResponse response) {
        setCookie(new String[] { token.getSeries(), token.getTokenValue() },
                getTokenValiditySeconds(), request, response);
    }
}
