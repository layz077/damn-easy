package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.repository.AccountDeletionRequestsRepository;
import com.example.demo.repository.ProfilePhotosRepository;
import com.example.demo.repository.UserRoleRepository;
import org.json.JSONObject;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.provisioning.UserDetailsManagerResourceFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.implementation.mailingService;
import com.example.demo.repository.UserRepository;
import com.example.demo.securityConfig.SecurityConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfilePhotosRepository profileRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private AccountDeletionRequestsRepository accountDeletionRequestsRepository;
	
	@Autowired
	private SecurityConfig securityConfig;
	private final mailingService mailingService = new mailingService();
	private ResponseEntity<?> response = null;

    @Value("${example}")
	private String example;

	@PostMapping(value = "/user/login", consumes ="application/json",produces="application/json")
	 public ResponseEntity<?> login(@RequestBody String input,HttpServletRequest request) throws Exception{

	  try {
		  User user = objectMapper.readValue(input, User.class);
//        JSONObject json = new JSONObject();

		  logger.info(example);
		  String phoneNumber = user.getPhoneNumber();


		  String password = user.getPassword();
		  boolean isPresent = false;
		  try {
			  String fromDb = userRepository.getHash(phoneNumber);
			  isPresent = securityConfig.passwordEncoder().matches(password, fromDb);
		  } catch (Exception e) {
			  logger.info("exception");
		  }

		  // First check if account is deleted
		  // Check for null pointer exception here later
		  if (userRepository.getIsDeleted(phoneNumber))
			  return ResponseEntity.status(HttpStatus.OK).body("User not present");

		  // Check if account is disabled,  (will make controller for disabling and enabling later)
          if(!userRepository.getIsActive(phoneNumber)){
			  if(accountDeletionRequestsRepository.getByPhone(phoneNumber)==null){
				  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account disabled please click on the below link for enabling process\n"+
						                                                      "http://localhost:8080/accountEnable/");
			  }
		  }

		  if (userRepository.getByPhone(phoneNumber) != null) {

			  if (isPresent) {

				  String emailId = userRepository.getEmail(phoneNumber);
				  userRepository.setLastIp(request.getRemoteAddr(), phoneNumber);
				  logger.info(userRepository.getIsActive(phoneNumber));

				  if (userRepository.getIsActive(phoneNumber)) {
//					mailingService.sendMail(emailId, "", "", "login", request);
					response = ResponseEntity.status(HttpStatus.OK).body("Logged in");

				  } else {
//					mailingService.sendMail(emailId, "", "", "login recover", request);
					  accountDeletionRequestsRepository.deleteById(phoneNumber);
					  response = ResponseEntity.status(HttpStatus.OK).body("Account Recovered Successfully. Logged In");
				  }

				  userRepository.recoverAccount(phoneNumber);

			  }
		  } else {
			  response = ResponseEntity.status(HttpStatus.OK).body("Enter a valid username or password");
		  }

	  }
	  catch (Exception e){
		  response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Some error occurred while processing your request");
		  e.printStackTrace();
	  }
		return response;
	 }




//	 @GetMapping("/user/login=true")
//	 public ModelAndView getData(String phoneNumber) {
//
//		 try {
//
//			 String role = userRoleRepository.getRole(phoneNumber);
//			 String userDetails = userRepository.getForLogin(phoneNumber);
//
//			 try {
//				 User user = objectMapper.readValue(userDetails, User.class);
//			 } catch (JsonProcessingException e) {
//				 e.printStackTrace();
//			 }
//
//			 if(Objects.equals(role, "ROLE_USER")){
//			 }
//
//			 return new ModelAndView("");
//
//		 } catch (Exception e) {
//			 return new ModelAndView("500");
//		 }
//	 }
}
