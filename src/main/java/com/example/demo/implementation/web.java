package com.example.demo.implementation;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.dtos.RegistrationDto;

public interface web {

	public String registration(RegistrationDto input,HttpServletRequest request);
}
