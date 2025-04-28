package com.example.server.Controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.server.Repository.UserRepository;
import com.example.server.Security.JwtProvider;
import com.example.server.entity.AppUser;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    
    public AuthController(AuthenticationManager authenticationManager, 
                           UserRepository userRepository, 
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signin")
    public String authenticateUser(@RequestBody AppUser appuser) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                appuser.getUsername(),
                appuser.getPassword()
            )
        );
        String jwt = JwtProvider.generateToken(authentication);
        return jwt;
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody AppUser user) {
        System.out.println("Inside registerUser method"); // Track if method is hit
        System.out.println("Incoming user: " + user);
    
        if (userRepository.existsByUsername(user.getUsername())) {
            System.out.println("Username already exists: " + user.getUsername());
            return "Error: Username is already taken!";
        }
    
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Encoded password: " + user.getPassword());
    
            userRepository.save(user);
            System.out.println(user + " saved to DB");
    
            return "User registered successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
}
