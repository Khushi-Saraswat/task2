package com.example.server.Security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.server.Repository.Rolerepo;
import com.example.server.Repository.UserRepository;
import com.example.server.entity.AppUser;
import com.example.server.entity.Role;
import com.example.server.entity.UserDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Rolerepo rolerepo;

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    System.out.println("inside custom user details service");
    System.out.println("username in user details service" + "" + email);
    AppUser user = userRepository.findByEmail(email);
    System.out.println("user" + "" + user + "in custome user details service");
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + user);
    }

    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
        mapRolesToAuthorities(user.getRoles()));
  }

  public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
  }

  public AppUser save(UserDto userregisterationdto) {

    Role role = null;
    if ("USER".equalsIgnoreCase(userregisterationdto.getRole())) {

      role = rolerepo.findByRole("ROLE_USER");

      if (role == null) {
        role = new Role("ROLE_USER"); // create new role if not found
        rolerepo.save(role);
      }
    }

    else if ("ADMIN".equalsIgnoreCase(userregisterationdto.getRole())) {

      role = rolerepo.findByRole("ROLE_ADMIN");

      if (role == null) {
        role = new Role("ROLE_ADMIN"); // create new role if not found
        rolerepo.save(role);
      }

    }

    AppUser user = new AppUser();
    user.setEmail(userregisterationdto.getEmail());
    user.setPassword(passwordEncoder.encode(userregisterationdto.getPassword()));
    user.setRoles(new HashSet<>(Collections.singleton(role)));
    user.setName(userregisterationdto.getName());

    return userRepository.save(user);

  }

}
