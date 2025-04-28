package com.example.server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Test")
public class TestController {
    
    @GetMapping("/all")
    public String allAccess(){
        return "Public content !";
    }

    @GetMapping("/user")
    public String UserAccess( @RequestHeader("Authorization") String jwt){
        return "User Content. ";
    }



}
