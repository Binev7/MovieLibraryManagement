package com.example.movielibrarymanagement.helper.mapper;
import com.example.movielibrarymanagement.dto.AuthResponseDto;
import com.example.movielibrarymanagement.dto.RegisterRequestDto;
import com.example.movielibrarymanagement.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        return user;
    }

    public AuthResponseDto toAuthResponseDto(User user) {
        return new AuthResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}