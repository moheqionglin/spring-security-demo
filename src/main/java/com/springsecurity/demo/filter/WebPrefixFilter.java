package com.springsecurity.demo.filter;

import com.springsecurity.demo.filter.holder.WebPrefixFilterHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author wanli zhou
 * @created 2017-10-29 9:55 AM.
 */
@Order(200)
@Scope("request")
@WebFilter(urlPatterns = {"/p/*", "/resources/*"})
public class WebPrefixFilter implements Filter{
    ApplicationContext applicationContext;
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[WebPrefixFilter] ==> call");
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String allFilterAfterAuthToken =  httpServletRequest.getHeader("web-prefix-filter");
        if(!StringUtils.isEmpty(allFilterAfterAuthToken)){
            log.info("Inject web-prefix-filter into WebPrefixFilterHolder");
            applicationContext.getBean(WebPrefixFilterHolder.class).setToken(allFilterAfterAuthToken);
        }
        log.info("[WebPrefixFilter] ==> filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
