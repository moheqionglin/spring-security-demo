package com.springsecurity.demo.Controller;

import com.springsecurity.demo.common.UserInfo;
import com.springsecurity.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * @author wanli zhou
 * @created 2017-10-30 9:54 AM.
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;

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
}
