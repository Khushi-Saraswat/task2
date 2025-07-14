package com.example.server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.Repository.UserRepository;
import com.example.server.Security.JwtProvider;
import com.example.server.entity.AppUser;

@Service
public class UserService {
    


     @Autowired
     UserRepository userRepository;




   public AppUser findUserProfileByJwt(String jwt){
    String email= JwtProvider.getEmailFromToken(jwt);
		
		
		AppUser user = userRepository.findByEmail(email);
		
		if(user==null) {
	
            System.out.println("user not exist with email"+email);
		}
		return user;
   }
}
