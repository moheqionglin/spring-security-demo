package com.springsecurity.demo.authentication;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Collection;

/**
 * @author wanli zhou
 * @created 2017-11-15 2:23 PM.
 */
public class SelfWebExpressionVoter extends WebExpressionVoter {
    private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

    @Override
    public int vote(Authentication authentication, FilterInvocation fi,
                    Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert fi != null;
        assert attributes != null;

        SelfWebExpressionConfigAttribute weca = findConfigAttribute(attributes);

        if (weca == null) {
            return ACCESS_ABSTAIN;
        }

        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication,
                fi);

        return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED
                : ACCESS_DENIED;
    }

    private SelfWebExpressionConfigAttribute findConfigAttribute(
            Collection<ConfigAttribute> attributes) {
        for (ConfigAttribute attribute : attributes) {
            if (attribute instanceof SelfWebExpressionConfigAttribute) {
                return (SelfWebExpressionConfigAttribute) attribute;
            }
        }
        return null;
    }

}
