package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.AuthResponseDto;
import com.example.movielibrarymanagement.dto.LoginRequestDto;
import com.example.movielibrarymanagement.dto.RegisterRequestDto;
import com.example.movielibrarymanagement.exception.ResourceAlreadyExistsException;
import com.example.movielibrarymanagement.helper.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken: " + registerRequest.getUsername());
        }

        User user = userMapper.toEntity(registerRequest);
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

        return userMapper.toAuthResponseDto(user);    }
}
