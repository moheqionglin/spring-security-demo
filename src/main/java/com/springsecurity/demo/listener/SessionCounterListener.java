package com.springsecurity.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/**
 * @author wanli zhou
 * @created 2017-11-06 11:26 PM.
 */

@Component
public class SessionCounterListener implements HttpSessionListener {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private static int totalActiveSessions;

    public static int getTotalActiveSession(){
        return totalActiveSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        totalActiveSessions++;
        log.info("sessionCreated - add one session into counter: {}", totalActiveSessions);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        totalActiveSessions--;
        log.info("sessionDestroyed - deduct one session from counter: {}", totalActiveSessions);
    }
}