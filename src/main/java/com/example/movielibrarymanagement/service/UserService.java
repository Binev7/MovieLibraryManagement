package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.AuthResponseDto;
import com.example.movielibrarymanagement.dto.LoginRequestDto;
import com.example.movielibrarymanagement.dto.RegisterRequestDto;

public interface UserService {
    AuthResponseDto register(RegisterRequestDto registerRequest);
    AuthResponseDto login(LoginRequestDto loginRequest);
}