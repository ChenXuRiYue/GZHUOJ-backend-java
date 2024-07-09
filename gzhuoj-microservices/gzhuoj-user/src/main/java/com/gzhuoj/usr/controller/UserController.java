package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    public UserService userService;
    @GetMapping("/login")
    public String userLogin(){
        return userService.test();
    }

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello ~");
        return "hello ~";
    }
}
