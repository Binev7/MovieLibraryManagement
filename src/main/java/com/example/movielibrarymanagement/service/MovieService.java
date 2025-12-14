package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.MovieRequestDto;
import com.example.movielibrarymanagement.dto.MovieResponseDto;

import java.util.List;

public interface MovieService {
    MovieResponseDto createMovie(MovieRequestDto requestDto);
    MovieResponseDto updateMovie(Long id, MovieRequestDto requestDto);
    List<MovieResponseDto> getAllMovies();
    MovieResponseDto getMovieById(Long id);
    void deleteMovie(Long id);
}