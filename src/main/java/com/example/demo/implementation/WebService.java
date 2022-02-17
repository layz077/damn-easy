package com.example.demo.implementation;

import com.example.demo.dtos.RegistrationDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.IContext;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
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
	@Autowired
	private ProfilePhotosRepository profileRepository;
	@Autowired
	private ProfilePhotosArchivedRepository profilePhotosArchivedRepository;

    private static ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private AccountDeletionRequestsRepository accountDeletionRequestsRepository;
    
    private static mailingService mail = new mailingService();


    private final PhoneCheck phoneCheck = new PhoneCheck();
    private final NameCheck nameCheck = new NameCheck();
//    private LocalDate dateTime = LocalDate.now();
//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	private final static Logger logger = Logger.getLogger(WebService.class);
	
    public String registration(MultipartFile file,String details, HttpServletRequest request){

    try {

		User input = null;

		try {
			input = objectMapper.readValue(details, User.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		User user = new User();
		Authorities user_roles = new Authorities();


		logger.info(request.getRemoteAddr());

		if (input.getName().length()==0 || input.getEmail().length() == 0 || input.getPassword().length() == 0) {
			return "Name (or) email (or) password cannot be empty";
		} else {
			if (!nameCheck.isValid(input.getName())) return "Special characters or number not allowed in name";
			else {
				if (!phoneCheck.isValid(input.getPhoneNumber()) || input.getPhoneNumber() == null || input.getPhoneNumber().length() != 10)
					return "Enter a valid phone number";
				else {
					if (input.getEmail().endsWith("@gmail.com") || input.getEmail().endsWith("@outlook.com") || input.getEmail().endsWith("@yahoo.com")) {
						if (input.getPassword().equals(null)) return "You must enter a password";
						if (userRepository.getByPhone(input.getPhoneNumber()) != null)
							return "Phone number already present";
						if (userRepository.getByEmail(input.getEmail()) != null) return "Email already present";
						user.setName(input.getName());
						user.setEmail(input.getEmail());
						user.setPhoneNumber(input.getPhoneNumber());

						// Creating UserName
						String userName = input.getEmail()
								.substring(0, input.getEmail().indexOf("@")) + "@" + input.getPhoneNumber()
								.substring(0, 5);
						user.setUserName(userName);

						// Encoding the password
						String password = securityConfig
								.passwordEncoder()
								.encode(input.getPassword());
						user.setPassword(password);
						logger.info(password);

//						   if(!Objects.equals(multipartFile.getContentType(), "image/jpeg")){
//							   return "Profile photo should be an image";
//						   }


						// String -> SQL date

						Date date = Date.valueOf(LocalDate.now());
						user.setCreatedOn(date);
						user.setEnabled(true);
						user.setDeleted(false);
						user = userRepository.saveAndFlush(user);


						// Image check and then insert

						if (!file.isEmpty()) {

							try {

								BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
								ImageIO.write(bufferedImage, "png", new File("D:\\images\\" + userName + ".png"));


								ProfilePhotos profile = new ProfilePhotos(user.getPhoneNumber(), "D:\\images\\" + userName + ".png",date,0);
								profileRepository.saveAndFlush(profile);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}


						user_roles.setPhonenumber(input.getPhoneNumber());
						user_roles.setRoleName("ROLE_USER");

						// Saving to Database

						userRoleRepository.saveAndFlush(user_roles);

						try {
							mail.sendMail(input.getEmail(), input.getName(), userName, "register", request);
						} catch (Exception e) {
							e.printStackTrace();
						}


					} else {
						return "Enter valid email id";
					}

				}
			}
		}

		return "Success";
	}
	catch (Exception e){
		return "Some error occurred while processing";
	}
    }
    
    public String updateProf(MultipartFile file ,String input, HttpServletRequest httpRequest) {
		
    	
    	/*
    	 * NAME and USERNAME and profile photo
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

				// Photo upload if present and also check if photo is updated or not

				if(!file.isEmpty()){
					ProfilePhotos photos;
					BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
					ProfilePhotos profilePhotos  = profileRepository.details(phoneNumber);

					if(profilePhotos!=null){

						// Moving previous data to archive
						profilePhotosArchivedRepository.insertData(profilePhotos.getPhoneNumber(),profilePhotos.getFileLocation(),profilePhotos.getUpdateCount(),profilePhotos.getUploadDate());

						int count = profileRepository.countValue(phoneNumber);

						if(count<1){

							ImageIO.write(bufferedImage,"png", new File("D:\\images\\" + username +"new"+(count+1)+".png"));
							photos = new ProfilePhotos(phoneNumber,"D:\\images\\" + username +"new"+(count+1)+ ".png",updatedOn,1);
						}

						else{

							ImageIO.write(bufferedImage,"png", new File("D:\\images\\" + username +"new"+(count+1)+ ".png"));
							photos = new ProfilePhotos(phoneNumber,"D:\\images\\" + username +"new"+(count+1)+ ".png",updatedOn,(count+1));

						}
					  }
					  else {

						ImageIO.write(bufferedImage,"png", new File("D:\\images\\" + username + ".png"));
						photos = new ProfilePhotos(phoneNumber,"D:\\images\\" + username + ".png",updatedOn,0);
					}

					profileRepository.saveAndFlush(photos);
				  }


				// Other details upload

		    		if(nameCheck.isValid(name)) {

		    			if(username.equals(userRepository.getUsername(phoneNumber))){

		    				logger.info(userRepository.getUsername(phoneNumber));

		    				userRepository.updateNameOnly(name,updatedOn,phoneNumber);
		    				mail.sendMail(email, name, "", "update", httpRequest);
		    				return "Details updated successfully";

		    			}
		    			else if (userRepository.ifUserNameAlreadyPresent(username) != null) {

								boolean isDeleted = userRepository.getDeletedByUsername(username);
								if (!isDeleted) {
									return "Username \"" + user.getUserName() + "\" already present.Please try another username";
								} else {
									userRepository.updateNameAndUser(name, updatedOn, username, phoneNumber);
									mail.sendMail(email, name, "", "update", httpRequest);
									return "Details updated successfully";
								}

		    			}
						else {
							userRepository.updateNameAndUser(name, updatedOn, username, phoneNumber);
							mail.sendMail(email, name, "", "update", httpRequest);
							return "Details updated successfully";
						}
		    			
		        	}
		        	else {
		        		return "Please enter a valid name";
		        	}
	        	
	    	}
	    	else {
	    		return "Fields cannot be null";
	    	}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return "Some error occurred while processing your request";
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
	    		return "Some error occurred while processing your request";
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
			if(accountDeletionRequestsRepository.getByPhone(user.getMobileNumber())!=null) return "Account already added to deletion request";
			userRepository.addAccountToDeleteRequest(user.getMobileNumber());
			accountDeletionRequestsRepository.saveAndFlush(user);
			
			return "Account deletion request processed successfully.\nTo reactivate your account just login to your account within 30 days.";
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Some error occurred while processing your request";
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

	public String recover(String id){

		if(phoneCheck.isValid(id)){

			String optionalAccountDeletionRequests = accountDeletionRequestsRepository.getByPhone(id);
			logger.info(optionalAccountDeletionRequests);
			if(optionalAccountDeletionRequests == null || optionalAccountDeletionRequests.length() ==0){
				return "Enter a valid phone number";
			}
			accountDeletionRequestsRepository.deleteById(id);
			userRepository.recoverAccount(id);
			mail.sendMail(
					userRepository.getEmail(id), "", "", "login recover", null);
			return "Account recovered successfully";
		}
		else return "Bad input";
	}

	public String imageTest(MultipartFile file,String details) {

		try {

			BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            ImageIO.write(bufferedImage,"png",new File("D:\\images\\photo1.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";

	}
}
