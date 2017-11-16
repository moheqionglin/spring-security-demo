package com.springsecurity.demo.resources;

import com.springsecurity.demo.common.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanli zhou
 * @created 2017-10-30 9:54 AM.
 */
@RestController
@RequestMapping("/resources/user")
public class UserRestResourceController {

    @Autowired
    private UserRestResourceService userService;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @GetMapping(value = "/all")
    public List<UserInfo> allUsers(){
        return userService.findAllUsers();
    }


    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public UserInfo findUserByName(@PathVariable("name") String name){
        return userService.findUserByName(name);
    }

    @PutMapping(value = "/name/{name}")
    @ResponseBody
    public UserInfo updateUserByName(@PathVariable("name") String name, @RequestBody UserInfo u){
        return userService.updateUserByName(name, u);
    }



}
