package com.example.demo.implementation;

import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class mailingService {
	
	private static final String companyEmail = "*****";
	private static final String password = "*****";
	private static final String host = "smtp.gmail.com";
	private static final String port = "465";
	private String subject;
	private String message;

		/*TYPE
		 * registration
		 * login
		 * or any other*/
	
	public void sendMail(String to,String name ,String userName, String type,HttpServletRequest request){
		
	          Properties properties = System.getProperties();
	          
	          properties.setProperty("mail.smtp.host", host);
	          properties.setProperty("mail.smtp.port", port);
	          properties.setProperty("mail.smtp.ssl.enable", "true");
	          properties.setProperty("mail.smtp.auth", "true");
	          
	          if(type.equalsIgnoreCase("register")) {
	        	  subject = "Welcome to our platform, " + name;
	        	  message = "Thank you for considering our platform.Your username is "+ userName +".\n If you think this mail is a mistake please click on the below link to deregister your mail.";
	          }
	          
	          else if(type.equalsIgnoreCase("login")) {
	        	  subject = "***New Sign In***";
	        	  message = "There is a new sign in from ip "+request.getRemoteAddr()+".\nIf not you please change the password";
	          }
	          
	         
	          Session session = Session.getInstance(properties, new Authenticator() {
			
	        	  @Override
	        	    public javax.mail.PasswordAuthentication getPasswordAuthentication() {
	        	    	return new javax.mail.PasswordAuthentication(companyEmail, password);
	        	    }
	          });
	          
	          MimeMessage mimeMessage = new MimeMessage(session);
	          
	          try {
				mimeMessage.setFrom(companyEmail);
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				mimeMessage.setSubject(subject);
				mimeMessage.setText(message);
				
				Transport.send(mimeMessage);
				
				
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	}
	
}
