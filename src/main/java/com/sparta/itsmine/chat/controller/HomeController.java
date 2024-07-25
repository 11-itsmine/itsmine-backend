package com.sparta.itsmine.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "mentoringRoom";
    }

    @GetMapping("/users/login-page")
    public String loginPage() {
        return "login";
    }
}