package com.example1.springbootdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloController {
    @RequestMapping("/Hello")
    public String Hello(){
        return "开始美团后端开发任务项目";
    }
}
