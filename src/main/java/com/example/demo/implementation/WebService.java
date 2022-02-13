package com.example.demo.implementation;

import com.example.demo.dtos.RegistrationDto;
import com.example.demo.entity.AccountDeletionRequests;
import com.example.demo.entity.Authorities;
import com.example.demo.entity.PasswordChange;
import com.example.demo.entity.Posts;
import com.example.demo.entity.User;
import com.example.demo.repository.AccountDeletionRequestsRepository;
import com.example.demo.repository.PostDetailsRepository;
import com.example.demo.repository.PostsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.securityConfig.SecurityConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.github.wnameless.json.flattener.JsonFlattener;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

class PhoneCheck{

   
    public boolean isValid(String s){
        Pattern pattern = Pattern.compile("[0-9]{10}");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}

class NameCheck{

   
    public boolean isValid(String s){
        Pattern pattern = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}

@Component
public class WebService implements web{

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostDetailsRepository postDetailsRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private AccountDeletionRequestsRepository accountDeletionRequestsRepository;
    
    private mailingService mail = new mailingService();


    private final PhoneCheck phoneCheck = new PhoneCheck();
    private final NameCheck nameCheck = new NameCheck();
//    private LocalDate dateTime = LocalDate.now();
//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	private final static Logger logger = Logger.getLogger(WebService.class);
	
    public String registration(RegistrationDto input,HttpServletRequest request){
    	

        User user = new User();
        Authorities user_roles = new Authorities();
    	

        logger.info(request.getRemoteAddr());
    	
        if(input.getName().length()==0 || input.getEmail().length()==0 || input.getPassword().length()==0){
            return "Name (or) email (or) password cannot be empty";
        }
        else {
               if(!nameCheck.isValid(input.getName())) return "Special characters or number not allowed in name";
               else {
                   if(!phoneCheck.isValid(input.getPhoneNumber()) || input.getPhoneNumber() == null || input.getPhoneNumber().length() != 10)  return "Enter a valid phone number";
                   else {
                       if(input.getEmail().endsWith("@gmail.com") || input.getEmail().endsWith("@outlook.com") || input.getEmail().endsWith("@yahoo.com") ) {
                    	   if(input.getPassword().equals(null)) return "You must enter a password";
                    	   if(userRepository.getByPhone(input.getPhoneNumber()) != null) return "Phone number already present";
                    	   if(userRepository.getByEmail(input.getEmail()) != null) return "Email already present";
                           user.setName(input.getName());
                           user.setEmail(input.getEmail());
                           user.setPhoneNumber(input.getPhoneNumber());

                           // Creating UserName
                           String userName = input.getEmail()
                        		                             .substring(0,input.getEmail().indexOf("@")) + "@"+ input.getPhoneNumber()
                        		                             .substring(0,5);
                           user.setUserName(userName);
                           
                           // Encoding the password
                           String password = securityConfig
                        		                           .passwordEncoder()
                        		                           .encode(input.getPassword());
                           user.setPassword(password);
                           logger.info(password);
                           
                           // String -> SQL date
                           
                           Date date  = Date.valueOf(LocalDate.now());
                           user.setCreatedOn(date);
                           user.setEnabled(true);
                           user.setDeleted(false);
                           userRepository.saveAndFlush(user);
                           
                           
                           user_roles.setPhonenumber(input.getPhoneNumber());
                           user_roles.setRoleName("ROLE_USER");                           
                           
                           // Saving to Database
                           
                           userRoleRepository.saveAndFlush(user_roles);
                           
                           try {
                        	   mail.sendMail(input.getEmail(), input.getName(), userName, "register" ,request);
                           } 
                           catch(Exception e) {
                        	   e.printStackTrace();
                           }
                           

                       }
                       else {
                    	   return "Enter valid email id";
                       }

                   }
               }
        }


        return "Success";
    }
    
    public String updateProf(String input, HttpServletRequest httpRequest) {
		
    	
    	/*
    	 * NAME and USERNAME
    	 * In client side on the update form previous details will be there already.
    	 * User will delete it and put new value which will come here.
    	 * So, if he only changes password previous name will come (Nothing blank will come)
    	 * */
    	
    	try {
              User user = new User();
        	
    		
    		try {
    			user = objectMapper.readValue(input, User.class);
    		} catch (JsonProcessingException e) {
    			e.printStackTrace();
    		} 	
    		
    		String phoneNumber = user.getPhoneNumber();
        	String name = user.getName();
        	String email = user.getEmail();
        	Date updatedOn = Date.valueOf(LocalDate.now());
    		
    	
	    	if(!email.equals(null) && !name.equals(null)) {
	    		
//	        	String username = user.getEmail()
//								                .substring(0,user.getEmail().indexOf("@")) + "@"+ user.getPhoneNumber()
//								                .substring(0,5);
	    		
	    		String username = user.getUserName();
	        	
	        	logger.info(username);
	    		
		    		if(nameCheck.isValid(name)) {
		    			
		    			if(username.equals(userRepository.getUsername(phoneNumber))){
		    				
		    				logger.info(userRepository.getUsername(phoneNumber));
		    					    				
		    				userRepository.updateNameOnly(name,updatedOn,phoneNumber);
		    				mail.sendMail(email, name, "", "update", httpRequest);
		    				return "Details updated successfully";
		    				
		    			}
		    			else {
		    				if(userRepository.ifUserNameAlreadyPresent(username)!= null) {
		    					return "Username \""+ user.getUserName() +"\" already present.Please try another username";
		    				}
		    				else {
		    					userRepository.updateNameAndUser(name,updatedOn,username, phoneNumber);
			    				mail.sendMail(email, name, "", "update", httpRequest);
			    				return "Details updated successfully";
		    				}
		    				
		    			}
		    			
		        	}
		        	else {
		        		return "Please enter a valid name or phone number";
		        	}
	        	
	    	}
	    	else {
	    		return "Fields cannot be null";
	    	}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return "Some error occured while processing your request";
    	}
    	
    }
    
    public String changePassword(String input) {
	
    	PasswordChange user = new PasswordChange();
    	
    	
    	try {
    		
    		try {
    			user = objectMapper.readValue(input, PasswordChange.class);
    			logger.info("OLD --> "+user.getPreviousPassword());
    			logger.info("NEW --> "+user.getNewPassword());
    		} 
        	catch (JsonProcessingException e) {
    			e.printStackTrace();
    		}    		
    	
    		   String fromDb = userRepository.getHash(user.getPhoneNumber());
    		   
    		   boolean matches = securityConfig.passwordEncoder().matches(user.getPreviousPassword(), fromDb);
    		   logger.info(matches);
    		   
    		   if(matches) { 
    			   
    			   if(user.getPreviousPassword().equals(user.getNewPassword())) return "New password must be different";
    			   
    			   String newPassword = securityConfig.passwordEncoder().encode(user.getNewPassword());
    			   userRepository.updatePassword(newPassword, user.getPhoneNumber());
    			   mail.sendMail(userRepository.getEmail(user.getPhoneNumber()), "", "", "password", null);
    			   return "Password changed successfully";
    			   
    		   }
    		   else {
    			   return "Enter correct password";
    		   }
    		 
    	}
    	catch(Exception e) {
	    		e.printStackTrace();
	    		return "Some error occured while processing your request";
    	}
		
    }
    
    public String deleteUser(String input) {
    	
    	/*
    	 * Deleted bit -> 0
    	 * enabled -> 1
    	 * So, that user can login and reactivate account.
    	 * After 30 days enabled bit -> 0 automatically.
    	 * */
    	
		Date date = Date.valueOf(LocalDate.now());
		Date expiryDate = Date.valueOf(LocalDate.now().plusDays(30));
		
		AccountDeletionRequests user = new AccountDeletionRequests();	
		
		try {
			
			user = objectMapper.readValue(input, AccountDeletionRequests.class);
			user.getMobileNumber();
			
			user.setDate(date);
			user.setPermanentDeleteDate(expiryDate);
			
			logger.info(user.toString());
			
			
			// only change enable for now
			userRepository.addAccountToDeleteRequest(true,true,user.getMobileNumber());
			accountDeletionRequestsRepository.saveAndFlush(user);
			
			return "Account deletion request proccessed successfully.\nTo reactivate your account just login to your account within 30 days.";
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Some error occured while processing your request";
		}
     
    }
    
    public void autoDelete() {
    	
    	Date date = Date.valueOf(LocalDate.now().plusDays(1));
    	
    	List<String> usersToWarn = accountDeletionRequestsRepository.getDetails(date);
    	logger.info(usersToWarn);
    	
    	
    	usersToWarn.forEach(phone->{
    		
    		String email  =  userRepository.getEmail(phone);
    		mail.sendMail(email, "", phone, "deletion warning", null);
    		
    	});
    	
    	Date dateDelete = Date.valueOf(LocalDate.now());
    	
    	List<String> usersToDelete = accountDeletionRequestsRepository.getDetails(dateDelete);
    	logger.info(usersToDelete);
    	
    	usersToDelete.forEach(phone->{
    		
    		String email =  userRepository.getEmail(phone);
    		mail.sendMail(email, "", phone, "delete", null);
    		
    		accountDeletionRequestsRepository.deleteEntry(dateDelete);
        	userRepository.deleteAccount(dateDelete, phone);
    	});
    	
    }    	
    	  
    

	public String followReq(String sender, String receiver) {
		
		return "";
		
	}
}
