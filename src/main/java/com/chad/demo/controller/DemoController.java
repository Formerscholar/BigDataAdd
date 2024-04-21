package com.chad.demo.controller;

import com.chad.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {
    @Resource
    private UserService userService;

    @GetMapping("/testAdd")
    private String test() {
        userService.addAll();
        return "ok";
    }

}
