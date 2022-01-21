package com.example.demo.controller;

import com.example.demo.dtos.RegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.implementation.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MainController {

    @Autowired
    private WebService webService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegistrationDto user, HttpServletRequest httpRequest){
        return webService.registration(user,httpRequest);
    }
    
    @PutMapping("/user/update")
    public String updateProfile(@RequestBody String input, HttpServletRequest httpRequest) {
    	return webService.updateProf(input,httpRequest);
    }
    
    @PostMapping("/user/changePassword")
    public String passChange(@RequestBody String input) {
    	return webService.changePassword(input);
    }
    
    @DeleteMapping("/user/delete")
    public String delUser(@RequestBody String phoneNumber) {
    	return webService.deleteUser(phoneNumber);
    }
    
    @DeleteMapping("/user/autoDelete")
    public void autoDeleteEveryDat() {
    	webService.autoDelete();
    }
}
