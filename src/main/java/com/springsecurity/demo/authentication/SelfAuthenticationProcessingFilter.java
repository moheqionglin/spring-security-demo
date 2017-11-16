package com.springsecurity.demo.authentication;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author wanli zhou
 * @created 2017-10-24 10:38 PM.
 * N9_1
 */
public class SelfAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter{


    private final static String LOGIN_URL = "/ajax_login";

    protected SelfAuthenticationProcessingFilter(AuthenticationProvider authenticationProvider) {
        super(LOGIN_URL);
//        this.setContinueChainBeforeSuccessfulAuthentication(true);
        this.setAuthenticationManager(new ProviderManager(Arrays.asList(authenticationProvider)));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (((HttpServletRequest) req).getMethod().equals("POST")) {
            super.doFilter(req, res, chain);
        } else {
            HttpServletResponse resp = (HttpServletResponse) res;
            resp.setHeader("Access-Control-Allow-Origin", "*");
            chain.doFilter(req, res);
        }
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)  {
        try {
            JsonObject authObj = Json.createReader(httpServletRequest.getInputStream()).readObject();
            String username = authObj.getString("username");
            String password = authObj.getString("password");
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new BadCredentialsException("Username or password is empty");
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            return this.getAuthenticationManager().authenticate(token);
        }catch ( IOException e){
            throw new AuthenticationServiceException("parse json error", e);
        }catch(AuthenticationException e){
            throw e;
        }
    }

}
