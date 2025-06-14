package com.example.server.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Repository.UserRepository;
import com.example.server.Response.LoginRes;
import com.example.server.Response.RegisterRes;
import com.example.server.Security.CustomUserDetailsService;
import com.example.server.Security.JwtProvider;
import com.example.server.entity.AppUser;
import com.example.server.entity.UserDto;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AppUser appuser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        appuser.getEmail(),
                        appuser.getPassword()));

        String jwt = JwtProvider.generateToken(authentication);
        Set<String> roles = JwtProvider.extractRoles(jwt);

        System.out.println("Inside LoginUser method");
        System.out.println("Incoming user: " + appuser);
        String message = roles + " is logged in";
        System.out.println(message + " message");

        return ResponseEntity.ok(new LoginRes(jwt, message));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        System.out.println("Inside registerUser method");
        System.out.println("Incoming user: " + userDto);

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.ok(new RegisterRes("User already exists in DB"));
        }

        AppUser user = customUserDetailsService.save(userDto);

        if (user == null) {
            return ResponseEntity.ok(new RegisterRes("User was not saved successfully"));
        } else {
            return ResponseEntity.ok(new RegisterRes("User saved successfully"));
        }
    }

    @GetMapping("/api/auth/role")
    public ResponseEntity<Map<String, String>> getRoleFromToken(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        Set<String> roles = JwtProvider.extractRoles(jwt);
        System.out.println("Extracted roles: " + roles);
        Map<String, String> response = new HashMap<>();
        if (roles.isEmpty()) {
            response.put("message", "No roles found in the token");
            return ResponseEntity.ok(response);
        }
        response.put("role", roles.iterator().next());
        return ResponseEntity.ok(response);
    }

}
