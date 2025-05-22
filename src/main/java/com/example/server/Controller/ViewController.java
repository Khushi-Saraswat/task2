package com.example.server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.server.Dto.UserDto;
import com.example.server.entity.AppUser;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new AppUser());
        return "login"; // returns login.html from templates
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register"; // points to register.html in templates/
    }

}
