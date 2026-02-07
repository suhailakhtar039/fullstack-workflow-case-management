package com.caseflow.service.impl;

import com.caseflow.domain.Role;
import com.caseflow.domain.User;
import com.caseflow.dto.RegisterRequest;
import com.caseflow.repository.RoleRepository;
import com.caseflow.repository.UserRepository;
import com.caseflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest request) throws IllegalAccessException {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalAccessException("Username: " + request.getUsername() + " already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role:" + request.getRole()));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        user.getRoles().add(role);
        userRepository.save(user);
    }
}
