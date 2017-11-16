package com.springsecurity.demo.authentication;

import com.springsecurity.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wanli zhou
 * @created 2017-11-14 10:24 PM.
 */
@Component
public class SelfFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private AuthService authService;

    @Override
    public List<ConfigAttribute> getAttributes(Object object) {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        List<ConfigAttribute> attributes  = getAttributesByURL(url);

        return attributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public List<ConfigAttribute> getAttributesByURL(String inputUrl){

        List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        String role = authService.getRoles(inputUrl);
        role = (role == null ? "permitAll()" : role);

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(role);
        SelfWebExpressionConfigAttribute conf = new SelfWebExpressionConfigAttribute(exp);
        attributes.add(conf);
        return attributes;
    }
}
