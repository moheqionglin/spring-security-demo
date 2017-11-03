package com.springsecurity.demo.filter;


import com.springsecurity.demo.filter.holder.AllFilterAfterAuthHolder;
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
@Order(101)
@Scope("request")
public class AllFilterAfterAuth implements Filter {
    ApplicationContext applicationContext;
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[AllFilterAfterAuth] ==> call");
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String allFilterAfterAuthToken =  httpServletRequest.getHeader("all-filter-after-auth-token");
        if(!StringUtils.isEmpty(allFilterAfterAuthToken)){
            log.info("Inject all-filter-after-auth-token into allFilterAfterAuthToken");
            applicationContext.getBean(AllFilterAfterAuthHolder.class).setToken(allFilterAfterAuthToken);
        }
        log.info("[AllFilterAfterAuth] ==> filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
