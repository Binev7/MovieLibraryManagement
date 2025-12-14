package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.AuthResponseDto;
import com.example.movielibrarymanagement.dto.LoginRequestDto;
import com.example.movielibrarymanagement.dto.RegisterRequestDto;
import com.example.movielibrarymanagement.exception.ResourceAlreadyExistsException;
import com.example.movielibrarymanagement.model.Role;
import com.example.movielibrarymanagement.model.User;
import com.example.movielibrarymanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken: " + registerRequest.getUsername());
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.ROLE_USER);

        User saved = userRepository.save(user);

        return new AuthResponseDto(saved.getId(), saved.getUsername(), saved.getRole().name());
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponseDto(user.getId(), user.getUsername(), user.getRole().name());
    }
}
