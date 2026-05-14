package com.security.moviesearchapi.service;

import com.security.moviesearchapi.dto.Entity.UserEntity;
import com.security.moviesearchapi.dto.RegisterRequest;
import com.security.moviesearchapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repository,
                       PasswordEncoder encoder) {

        this.repository = repository;
        this.encoder = encoder;
    }

    public String register(RegisterRequest request) {

        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(
                encoder.encode(request.getPassword())
        );

        repository.save(user);

        return "User registered successfully";
    }
}