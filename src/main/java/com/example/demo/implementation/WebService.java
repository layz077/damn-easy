package com.example.demo.implementation;

import com.example.demo.dtos.RegistrationDto;
import com.example.demo.entity.Authorities;
import com.example.demo.entity.Posts;
import com.example.demo.entity.User;
import com.example.demo.repository.PostDetailsRepository;
import com.example.demo.repository.PostsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.securityConfig.SecurityConfig;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

    private User user = new User();
    private Authorities user_roles = new Authorities();
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostDetailsRepository postDetailsRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    private mailingService mail = new mailingService();


    private final PhoneCheck phoneCheck = new PhoneCheck();
    private final NameCheck nameCheck = new NameCheck();
//    private LocalDate dateTime = LocalDate.now();
//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	private final static Logger logger = Logger.getLogger(WebService.class);
	
    public String registration(RegistrationDto input,HttpServletRequest request){
    	

        logger.info(request.getRemoteAddr());
    	
        if(input.getName().length()==0 || input.getEmail().length()==0 || input.getPassword().length()==0){
            return "Name (or) email (or) password cannot be empty";
        }
        else {
               if(!nameCheck.isValid(input.getName())) return "Special characters or number not allowed in name";
               else {
                   if(!phoneCheck.isValid(input.getPhoneNumber()) || input.getPhoneNumber() == null)  return "Enter a valid phone number";
                   else {
                       if(input.getEmail().endsWith("@gmail.com") || input.getEmail().endsWith("@outlook.com") || input.getEmail().endsWith("@yahoo.com") ) {
                    	   if(input.getPassword().equals(null)) return "You must enter a password";
                    	   if(userRepository.getByPhone(input.getPhoneNumber()) != null) return "Phone number already present";
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
                           user_roles.setUsername(userName);
                           user_roles.setRoleName("ROLE_USER");                           
                           
                           // Saving to Database
                           userRepository.save(user);
                           userRoleRepository.save(user_roles);
                           
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
}
