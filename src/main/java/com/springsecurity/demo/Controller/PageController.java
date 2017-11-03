package com.springsecurity.demo.Controller;

import com.springsecurity.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wanli zhou
 * @created 2017-10-30 10:15 PM.
 */
@Controller
public class PageController {

    @Autowired
    private UserService userService;
    Logger log = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/")
    public String directIndex(HttpServletResponse rep) {
        return "index";
    }

    @RequestMapping("/p/")
    public String direct() {
        log.trace("Default page requested, rendering index.");
        return "index";
    }
    @GetMapping("/p/{page}")
    public String direct(@PathVariable("page") String page, Map<String, Object> model) {
        if("manager-user".equals(page)){
            model.put("allUsers", userService.findAllUsers());
        }
        log.trace("Trying to render page: {}", page);
        return page;
    }

}
