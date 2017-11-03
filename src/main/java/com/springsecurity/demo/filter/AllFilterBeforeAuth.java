package com.springsecurity.demo.filter;

import com.springsecurity.demo.filter.holder.AllFilterBeforeAuthHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author wanli zhou
 * @created 2017-10-29 9:55 AM.
 */
@Order(1)
@Scope("request")
public class AllFilterBeforeAuth implements Filter {
    ApplicationContext applicationContext;
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[AllFilterBeforeAuth] ==> call");
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String allFilterAfterAuthToken =  httpServletRequest.getHeader("all-filter-before-auth-token");
        if(!StringUtils.isEmpty(allFilterAfterAuthToken)){
            log.info("Inject all-filter-before-auth-token into AllFilterBeforeAuthHolder");
            applicationContext.getBean(AllFilterBeforeAuthHolder.class).setToken(allFilterAfterAuthToken);
        }
        log.info("[AllFilterBeforeAuth] ==> filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
