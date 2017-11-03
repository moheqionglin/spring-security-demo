package com.springsecurity.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.annotation.WebFilter;
import java.util.Arrays;

/**
 * @author wanli zhou
 * @created 2017-10-30 9:40 PM.
 */
@Configuration
public class SelfFilterConfig {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public FilterRegistrationBean allFilterAfterAuth(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AllFilterAfterAuth());
        Order order = AllFilterAfterAuth.class.getAnnotation(Order.class);
        log.info(">>[order]<< = {}", order.value());
        filterRegistrationBean.setOrder(order.value());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean allFilterBeforeAuth(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AllFilterBeforeAuth());
        Order order = AllFilterBeforeAuth.class.getAnnotation(Order.class);
        log.info(">>[order]<< = {}", order.value());
        filterRegistrationBean.setOrder(order.value());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean webPrefixFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebPrefixFilter());

        Order order = WebPrefixFilter.class.getAnnotation(Order.class);
        filterRegistrationBean.setOrder(order.value());

        WebFilter webfilter = WebPrefixFilter.class.getAnnotation(WebFilter.class);
        filterRegistrationBean.setUrlPatterns(Arrays.asList(webfilter.urlPatterns()));
        log.info(">>[order]<< = {}, {}", order.value(), webfilter.urlPatterns());
        return filterRegistrationBean;
    }
}
