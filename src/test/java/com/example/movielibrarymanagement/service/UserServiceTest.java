package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.AuthResponseDto;
import com.example.movielibrarymanagement.dto.LoginRequestDto;
import com.example.movielibrarymanagement.dto.RegisterRequestDto;
import com.example.movielibrarymanagement.exception.ResourceAlreadyExistsException;
import com.example.movielibrarymanagement.helper.mapper.UserMapper;
import com.example.movielibrarymanagement.model.Role;
import com.example.movielibrarymanagement.model.User;
import com.example.movielibrarymanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(
                userRepository,
                passwordEncoder,
                userMapper
        );
    }


    @Test
    void register_ShouldCreateUser_WhenUsernameNotTaken() {
        RegisterRequestDto req = new RegisterRequestDto();
        req.setUsername("alice");
        req.setPassword("password123");

        when(userRepository.existsByUsername("alice")).thenReturn(false);

        when(userRepository.save(any())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        AuthResponseDto resp = userService.register(req);

        assertNotNull(resp);
        assertEquals("alice", resp.getUsername());
        assertEquals(Role.ROLE_USER.name(), resp.getRole());
        verify(userRepository).save(any());
    }

    @Test
    void register_ShouldThrow_WhenUsernameTaken() {
        RegisterRequestDto req = new RegisterRequestDto();
        req.setUsername("bob");
        req.setPassword("password");

        when(userRepository.existsByUsername("bob")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.register(req));
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsValid() {
        LoginRequestDto req = new LoginRequestDto();
        req.setUsername("carol");
        req.setPassword("secret");

        User user = new User(2L, "carol", passwordEncoder.encode("secret"), Role.ROLE_USER);
        when(userRepository.findByUsername("carol")).thenReturn(Optional.of(user));

        AuthResponseDto resp = userService.login(req);

        assertNotNull(resp);
        assertEquals("carol", resp.getUsername());
    }

    @Test
    void login_ShouldThrow_WhenInvalidCredentials() {
        LoginRequestDto req = new LoginRequestDto();
        req.setUsername("dave");
        req.setPassword("wrong");

        when(userRepository.findByUsername("dave")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login(req));
    }
}

