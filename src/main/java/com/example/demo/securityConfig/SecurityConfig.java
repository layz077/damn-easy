package com.example.demo.securityConfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private DataSource dataSource; 
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
	     http.authorizeRequests()
	         .antMatchers("/login","/register","/","/user/login","/user/delete","/user/autoDelete").permitAll()
	         .antMatchers("/home","/user/changePassword","/user/update").hasAnyAuthority("ROLE_USER")
	         .antMatchers("/homeAdmin").hasAuthority("ROLE_ADMIN")
	         .anyRequest()
	         .authenticated()
	         .and()
	         .formLogin()
//	         .loginPage("/login")
	         .failureUrl("/login?error=true")
//	         .loginProcessingUrl("/verifyUser") 
//	         .defaultSuccessUrl("/home?login=success")
	         .successHandler(loginSuccessHandler);
	         ;
	     http.csrf().disable();
	       
	     
	}
	

	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication()
//		                         .dataSource(dataSource)
//		                         .passwordEncoder(new BCryptPasswordEncoder())
//	                             .usersByUsernameQuery("SELECT username,password,enabled from user where username=?")
//	                             .authoritiesByUsernameQuery("SELECT username,rolename from user_roles where username=?");
//	}
}
