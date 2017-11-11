package com.springsecurity.demo.Controller;

import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanli zhou
 * @created 2017-10-30 9:54 AM.
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionRegistry sessionRegistry;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping(value = "/all")
    public List<UserInfo> allUsers(){
        return userService.findAllUsers();
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public UserInfo findUserByName(@PathVariable("name") String name){
        return userService.findUserByName(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/name/{name}")
    @ResponseBody
    public UserInfo updateUserByName(@PathVariable("name") String name, @RequestBody UserInfo u){
        return userService.updateUserByName(name, u);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping(value = "/name/{name}")
    @ResponseBody
    public String deleteUserByName(@PathVariable("name") String name){
         userService.deleteUserByName(name);
         return "success";
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping(value = "/forceLogout/{email}")
    @ResponseBody
    public String forceLogout(@PathVariable("email") String email){
        UserInfo superAdminUser = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Object> userInfoLists = sessionRegistry.getAllPrincipals();
        List<UserInfo> users = userInfoLists.stream().map(o -> (UserInfo) o).collect(Collectors.toList());
        users.stream().filter(o -> o instanceof UserInfo && ((UserInfo)o).getEmail().equals(email)).forEach(user -> {
            List<SessionInformation> allSessionIds = sessionRegistry.getAllSessions(user, false);
            if(allSessionIds != null){
                allSessionIds.stream().forEach(session -> {
                    session.expireNow();
                    sessionRegistry.removeSessionInformation(session.getSessionId());
                    log.info( "{} [session.getSessionId()] 被管理员 {} 踢出。",email,  superAdminUser.getEmail());
                });
            }
        });
        return "success";
    }

}
