package com.springsecurity.demo.authentication.n5_sessionManager_anonymousAuth_exceptionTrans;

import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author wanli zhou
 * @created 2017-10-28 9:33 PM.
 */
public class N13AnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {
    public N13AnonymousAuthenticationFilter(String key) {
        super(key);
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}
