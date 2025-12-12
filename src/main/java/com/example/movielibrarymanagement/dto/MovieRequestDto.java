package com.example.movielibrarymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String director;

    @Min(value = 1900, message = "Release year must be greater than 1900")
    private Integer releaseYear;
}
