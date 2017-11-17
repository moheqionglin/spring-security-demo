package com.springsecurity.demo.Controller;

import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wanli zhou
 * @created 2017-10-30 10:15 PM.
 */
@Controller
public class PageController {

    @Autowired
    private UserService userService;
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionRegistry sessionRegistry;

    @RequestMapping("/")
    public String directIndex(HttpServletResponse rep) {
        return "index";
    }

    @RequestMapping("/p/")
    public String direct() {
        log.trace("Default page requested, rendering index.");
        return "index";
    }


    @GetMapping("/p/all-login-users")
    public String allLoginUsers(Map<String, Object> model) {
        List<Object> userInfoLists = sessionRegistry.getAllPrincipals();
        List<UserInfo> users = userInfoLists.stream()
                .filter(o -> o instanceof UserInfo)
                .filter(user -> sessionRegistry.getAllSessions(user, false).size() > 0)
                .map(o -> (UserInfo) o).collect(Collectors.toList());
        model.put("allUsers", users);
        log.trace("Trying to render page: {}", users);
        return "all-login-users";
    }


    @GetMapping("/p/manager-user")
    public String direct(Map<String, Object> model) {
        model.put("allUsers", userService.findAllUsers());
        return "manager-user";
    }

    @GetMapping("/p/invalidSession")
    public String invalidSession() {
        return "invalidSession";
    }
    @GetMapping("/p/sessiontimeout")
    public String sessiontimeout() {
        return "sessiontimeout";
    }
    @GetMapping("/p/login")
    public String login() {
        return "login";
    }
    @GetMapping("/p/find-users")
    public String findUsers() {
        return "find-users";
    }
}
