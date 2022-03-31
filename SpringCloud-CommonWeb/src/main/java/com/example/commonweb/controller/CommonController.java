package com.example.commonweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
