package com.example.demo.controller;

import com.example.demo.dtos.RegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.implementation.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class Controller {

    @Autowired
    private WebService webService;

    @PostMapping("/user/register")
    public String register(@RequestBody @Valid RegistrationDto user, HttpServletRequest httpRequest){
        return webService.registration(user,httpRequest);
    }
}
