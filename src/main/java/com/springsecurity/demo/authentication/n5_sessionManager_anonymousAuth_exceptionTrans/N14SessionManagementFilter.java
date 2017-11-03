package com.springsecurity.demo.authentication.n5_sessionManager_anonymousAuth_exceptionTrans;

import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author wanli zhou
 * @created 2017-10-28 9:34 PM.
 */
public class N14SessionManagementFilter extends SessionManagementFilter {
    public N14SessionManagementFilter(SecurityContextRepository securityContextRepository) {
        super(securityContextRepository);
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}
