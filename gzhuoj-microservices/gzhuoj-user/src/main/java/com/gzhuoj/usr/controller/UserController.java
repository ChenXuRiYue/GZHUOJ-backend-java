package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.K;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    @GetMapping("/login")
    public void userLogin(){
        userService.test();
    }

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello ~");
        return "hello ~";
    }
}
