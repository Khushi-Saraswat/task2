package com.example.server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.server.Repository.UserRepository;
import com.example.server.entity.AppUser;
import com.example.server.entity.USER_ROLE;

@Component
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializationComponent(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminUsername = "khushisar45@gmail.com";

        if (userRepository.findByEmail(adminUsername) == null) {
            AppUser adminUser = new AppUser();

            adminUser.setPassword(passwordEncoder.encode("khushi123"));
            adminUser.setName("khushi saraswat");
            adminUser.setEmail(adminUsername);
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);
            AppUser admin = userRepository.save(adminUser);
        }
    }

}
