package com.example.movielibrarymanagement.helper.mapper;

import com.example.movielibrarymanagement.dto.MovieRequestDto;
import com.example.movielibrarymanagement.dto.MovieResponseDto;
import com.example.movielibrarymanagement.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieResponseDto toResponseDto(Movie movie) {
        MovieResponseDto dto = new MovieResponseDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDirector(movie.getDirector());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setRating(movie.getRating());
        return dto;
    }

    public Movie toEntity(MovieRequestDto requestDto) {
        Movie movie = new Movie();
        movie.setTitle(requestDto.getTitle());
        movie.setDirector(requestDto.getDirector());
        movie.setReleaseYear(requestDto.getReleaseYear());
        return movie;
    }
}


