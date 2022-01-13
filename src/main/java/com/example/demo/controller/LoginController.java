package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.provisioning.UserDetailsManagerResourceFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.securityConfig.SecurityConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

@RestController
public class LoginController {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	private static Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SecurityConfig securityConfig;

	@PostMapping(value = "/user/login", consumes ="application/json",produces="application/json")
	 public ResponseEntity<?> login(@RequestBody String input) throws Exception{

        User user = objectMapper.readValue(input, User.class);
//        JSONObject json = new JSONObject();
        
        logger.info(user);
        
        long id = user.getId();
        String fromDb = userRepository.getHash(id);
        String password = user.getPassword();
        
        boolean isPresent = securityConfig.passwordEncoder().matches(password, fromDb);
        
        if(isPresent) return ResponseEntity.status(HttpStatus.OK).body("Success");
        
        
//        if(userRepository.getUser(userName, password) == null) {
//        	
////        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json.put("message", "Enter a valid username or password"));
//        	return ResponseEntity.status(HttpStatus.OK).body("no");
//        }
        
//       json.put("message", "Logged in sucessfully");
//       json.put("Privilege", "Normal user");
        
		
		return ResponseEntity.status(HttpStatus.OK).body("Enter a valid username or password");
	 }
}
