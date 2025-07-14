package com.example.server.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.server.Service.UserService;
import com.example.server.entity.AppUser;
import com.example.server.entity.UserDto;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService customUserDetailsService,
            UserService userService

    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AppUser appuser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        appuser.getEmail(),
                        appuser.getPassword()));

        AppUser user = userRepository.findByEmail(appuser.getEmail());
        String jwt = JwtProvider.generateToken(authentication);

        System.out.println("Inside LoginUser method");
        System.out.println(" user: " + user);
        String role = user.getRole().toString();
        System.out.println("role in login" + role);
        return ResponseEntity.ok(new LoginRes(jwt, role));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        System.out.println("Inside registerUser method");
        System.out.println("Incoming user: " + userDto);

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.ok(new RegisterRes("User already exists in DB"));
        }

        AppUser user = customUserDetailsService.save(userDto);
        System.out.println("User after save: " + user);

        if (user == null) {
            return ResponseEntity.ok(new RegisterRes("User was not saved successfully"));
        } else {
            return ResponseEntity.ok(new RegisterRes("User saved successfully"));
        }
    }

    @GetMapping("/api/auth/role")
    public ResponseEntity<String> getRoleFromToken(@RequestHeader("Authorization") String token) {
        try {

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            } else {
                return ResponseEntity.badRequest().body("Invalid Authorization header");
            }

            String email = JwtProvider.getEmailFromToken(token);

            System.out.println(email + " <- email extracted from token");

            AppUser user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for email: " + email);
            }

            String role = user.getRole().toString();
            System.out.println(role + "role");
            return ResponseEntity.ok(role);

        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Malformed JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error parsing token: " + e.getMessage());
        }
    }

}
