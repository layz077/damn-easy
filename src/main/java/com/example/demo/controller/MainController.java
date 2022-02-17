package com.example.demo.controller;

import com.example.demo.dtos.RegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.implementation.WebService;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
public class MainController {

    @Autowired
    private WebService webService;
    private static final Logger logger = Logger.getLogger(MainController.class);


    @PostMapping(value = "/register",consumes = {"multipart/form-data"})
    public String register(@RequestPart("file") MultipartFile file,@RequestPart("details") @Valid String user, HttpServletRequest httpRequest){

//     if(!file.isEmpty()){
//         if(!Objects.equals(file.getContentType(), "image/png") || !Objects.equals(file.getContentType(), "image/jpeg")){
//             return "Profile photo must be an image";
//         }
//     }
        return webService.registration(file,user,httpRequest);

    }

    
    @PutMapping(value = "/user/update",consumes = {"multipart/form-data"})
    public String updateProfile(@RequestPart("file") MultipartFile image,@RequestPart("details") String input, HttpServletRequest httpRequest) {

//        if(!image.isEmpty()){
//            if(!Objects.equals(image.getContentType(), "image/png") || !Objects.equals(image.getContentType(), "image/jpeg")){
//                return "Profile photo must be an image";
//            }
//        }
    	return webService.updateProf(image,input,httpRequest);
    }
    
    @PostMapping("/user/changePassword")
    public String passChange(@RequestBody String input) {
    	return webService.changePassword(input);
    }
    
    @DeleteMapping("/user/delete")
    public String delUser(@RequestBody String phoneNumber) {
    	return webService.deleteUser(phoneNumber);
    }
    
    @DeleteMapping("/user/autoDelete")
    public void autoDeleteEveryDat() {
    	webService.autoDelete();
    }
    
    @PostMapping("/user/follow")
    public String followRequests(String sender,String receiver) {
    	return webService.followReq(sender,receiver);
    }

    @GetMapping("/recoverAccount/{id}")
    public String recover(@PathVariable("id") String id){
        return webService.recover(id);
    }


    @PostMapping(path="/test/image",consumes = {"multipart/form-data"})
    public String testImage(@RequestPart("file") MultipartFile image, @RequestPart("details") String dto){

//         MultipartFile image =  dto.getFile();
           logger.info(dto);
           return webService.imageTest(image,dto);

       }
}
