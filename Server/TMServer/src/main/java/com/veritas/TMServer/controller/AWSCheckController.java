package com.veritas.TMServer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AWSCheckController {
    @GetMapping("/")
    public String healthCheck(){
        return "The Service is running...";
    }
}
