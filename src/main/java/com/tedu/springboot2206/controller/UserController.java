package com.tedu.springboot2206.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理注册！！！！！");

    }

}
