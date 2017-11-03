package com.springsecurity.demo.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wanli zhou
 * @created 2017-10-24 11:06 PM.
 */
public class AuthCookieUtil {

    public static final String NAME = "AuthToken";
    public static final int TWO_MONTHS_IN_SEC = 60*60*24*60;
    public static String SESSION_NAME = "JSESSIONID";

    public static void removeAuthCookie(HttpServletResponse resp){
        //remove the auth cookie if they have it.
        Cookie cookie = new Cookie(NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        cookie = new Cookie(SESSION_NAME, "");
        cookie.setPath("/frontend-app/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }
}