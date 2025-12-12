package com.example.movielibrarymanagement.controller;

import com.example.movielibrarymanagement.dto.MovieRequestDto;
import com.example.movielibrarymanagement.dto.MovieResponseDto;
import com.example.movielibrarymanagement.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movie Management", description = "APIs for managing movies in the library")
@SecurityRequirement(name = "basicAuth")
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Creates a new movie and asynchronously fetches its IMDB rating. Requires ADMIN role.")
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieRequestDto requestDto) {
        MovieResponseDto response = movieService.createMovie(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieves all movies from the library")
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieves a specific movie by its ID")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id) {
        MovieResponseDto movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a movie", description = "Updates details of an existing movie. If the title changes, the rating is re-fetched asynchronously. Requires ADMIN role.")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequestDto requestDto) {
        MovieResponseDto updatedMovie = movieService.updateMovie(id, requestDto);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie", description = "Deletes a movie from the library. Requires ADMIN role.")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}