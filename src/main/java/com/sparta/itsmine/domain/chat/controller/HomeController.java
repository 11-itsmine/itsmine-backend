package com.sparta.itsmine.domain.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/chat")
	public String getDisplayPage() {
		return "index";
	}

	@GetMapping("/users/login-page")
	public String loginPage() {
		return "login";
	}

}

