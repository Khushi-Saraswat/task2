package com.example.server.Security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.server.Repository.UserRepository;
import com.example.server.entity.AppUser;
import com.example.server.entity.UserDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    System.out.println("Inside custom user details service");
    System.out.println("Email used: " + email);

    AppUser user = userRepository.findByEmail(email);
    System.out.println("Fetched user: " + user);

    if (user == null) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        authorities);
  }

  public AppUser save(UserDto userDto) {

    AppUser user = new AppUser();
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setName(userDto.getName());
    user.setEmail(userDto.getEmail());

    AppUser savedUser = userRepository.save(user);
    System.out.println("Saved user: " + savedUser);
    return savedUser;
  }
}
