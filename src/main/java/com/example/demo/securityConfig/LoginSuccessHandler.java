package com.example.demo.securityConfig;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.auth.ApplicationUser;

@Configuration
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	private static Logger logger = Logger.getLogger(LoginSuccessHandler.class);
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
		
		    ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
		    String redirectURL = request.getContextPath();
		    logger.info(redirectURL);
		    
		    Collection<? extends GrantedAuthority> rolesCollection = user.getAuthorities();
		    
		    String role = rolesCollection.toString();
		    
		    if(role.equals("[ROLE_USER]")){
		    	redirectURL += "/home";	
		    	
		    }
		    else if(role.equals("[ROLE_ADMIN]")) {
		    	System.out.println("Correct");
		    	redirectURL += "/homeAdmin";
		    }
		   
		    response.sendRedirect(redirectURL);
		    logger.info(redirectURL);
		    
		    
	}
	
}
