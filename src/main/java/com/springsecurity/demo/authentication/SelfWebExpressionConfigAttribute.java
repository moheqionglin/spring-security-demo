package com.springsecurity.demo.authentication;

import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

/**
 * @author wanli zhou
 * @created 2017-11-15 1:40 PM.
 */
public class SelfWebExpressionConfigAttribute implements ConfigAttribute {
    private final Expression authorizeExpression;

    public SelfWebExpressionConfigAttribute(Expression spelExpression) {
        this.authorizeExpression = spelExpression;
    }

    Expression getAuthorizeExpression() {
        return this.authorizeExpression;
    }


    @Override
    public String getAttribute() {
        return null;
    }

    @Override
    public String toString() {
        return this.authorizeExpression.getExpressionString();
    }
}
