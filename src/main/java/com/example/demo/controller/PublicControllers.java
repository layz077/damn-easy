package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller              // ResponseBody
public class PublicControllers {
	
	// Use Controller annotation instead of restController to return home.html
	
	@GetMapping("/")
	public String page() {
		return "page";
	}
	
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}

	// new ModelAndView("home.html");
}
