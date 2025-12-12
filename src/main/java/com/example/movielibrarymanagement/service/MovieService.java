package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.MovieRequestDto;
import com.example.movielibrarymanagement.dto.MovieResponseDto;
import com.example.movielibrarymanagement.exception.ResourceNotFoundException;
import com.example.movielibrarymanagement.helper.mapper.MovieMapper;
import com.example.movielibrarymanagement.model.Movie;
import com.example.movielibrarymanagement.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final RatingEnrichmentService ratingEnrichmentService;
    private final MovieMapper movieMapper;


    @Transactional
    public MovieResponseDto createMovie(MovieRequestDto requestDto) {
        Movie movie = movieMapper.toEntity(requestDto);

        Movie savedMovie = movieRepository.save(movie);
        log.info("Movie created with ID: {}", savedMovie.getId());

        ratingEnrichmentService.enrich(savedMovie.getId(), savedMovie.getTitle());

        return movieMapper.toResponseDto(savedMovie);
    }

    @Transactional
    public MovieResponseDto updateMovie(Long id, MovieRequestDto requestDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        boolean titleChanged = !movie.getTitle().equals(requestDto.getTitle());

        movie.setTitle(requestDto.getTitle());
        movie.setDirector(requestDto.getDirector());
        movie.setReleaseYear(requestDto.getReleaseYear());

        Movie updatedMovie = movieRepository.save(movie);
        log.info("Movie updated with ID: {}", updatedMovie.getId());

        if (titleChanged) {
            log.info("Title changed for movie ID: {}. Triggering rating enrichment.", updatedMovie.getId());
            ratingEnrichmentService.enrich(updatedMovie.getId(), updatedMovie.getTitle());
        }

        return movieMapper.toResponseDto(updatedMovie);
    }

    public List<MovieResponseDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return movieMapper.toResponseDto(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
        log.info("Movie deleted with ID: {}", id);
    }
}