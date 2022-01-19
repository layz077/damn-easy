
  package com.example.demo.auth;
  
  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.security.core.userdetails.UserDetails; import
  org.springframework.security.core.userdetails.UserDetailsService; import
  org.springframework.security.core.userdetails.UsernameNotFoundException;
  import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.entity.ValidationClass;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
  
  @Service public class ApplicationUserService implements UserDetailsService{
  
  @Autowired private UserRepository userRepository;
  @Autowired private UserRoleRepository userRoleRepository;
  
  @Override 
  public UserDetails loadUserByUsername(String username) throws  UsernameNotFoundException {
	  
	  ValidationClass validationClass = new ValidationClass();
	  User user = new User();
	  
	  
	  if(userRepository.findByUserName(username)== null) {throw new  UsernameNotFoundException("User " + username +" Not Found");}
	  
	  user = userRepository.findByUserName(username);
	  String role = userRoleRepository.getRole(username);
	  
	  validationClass.setUsername(username);
	  validationClass.setPassword(user.getPassword());
	  validationClass.setEnabled(user.isEnabled());
	  validationClass.setRole(role);
	  
	  return new ApplicationUser(validationClass); 
  }
  
  }
 
