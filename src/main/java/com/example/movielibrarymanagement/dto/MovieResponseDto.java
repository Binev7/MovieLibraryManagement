package com.example.movielibrarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDto {

    private Long id;
    private String title;
    private String director;
    private Integer releaseYear;
    private Double rating;
}