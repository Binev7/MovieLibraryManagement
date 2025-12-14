package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.dto.MovieRequestDto;
import com.example.movielibrarymanagement.dto.MovieResponseDto;
import com.example.movielibrarymanagement.exception.ResourceNotFoundException;
import com.example.movielibrarymanagement.helper.mapper.MovieMapper;
import com.example.movielibrarymanagement.model.Movie;
import com.example.movielibrarymanagement.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RatingEnrichmentService ratingEnrichmentService;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    @Test
    void createMovie_ShouldSaveAndReturnMovie() {
        MovieRequestDto requestDto = new MovieRequestDto("Inception", "Nolan", 2010);
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        MovieResponseDto responseDto = new MovieResponseDto(1L, "Inception", "Nolan", 2010, null);

        when(movieMapper.toEntity(requestDto)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toResponseDto(movie)).thenReturn(responseDto);

        MovieResponseDto result = movieService.createMovie(requestDto);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());

        verify(movieRepository).save(movie);
        verify(ratingEnrichmentService).enrich(1L, "Inception");
    }

    @Test
    void getAllMovies_ShouldReturnList() {
        Movie movie = new Movie();
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        when(movieMapper.toResponseDto(movie)).thenReturn(new MovieResponseDto());

        List<MovieResponseDto> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(movieRepository).findAll();
    }

    @Test
    void getMovieById_WhenExists_ShouldReturnMovie() {
        Long id = 1L;
        Movie movie = new Movie();
        movie.setId(id);

        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieMapper.toResponseDto(movie)).thenReturn(new MovieResponseDto(id, "Title", "Director", 2020, 5.0));

        MovieResponseDto result = movieService.getMovieById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getMovieById_WhenNotExists_ShouldThrowException() {
        Long id = 99L;
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieById(id));
    }

    @Test
    void updateMovie_WhenTitleChanged_ShouldTriggerEnrichment() {
        Long id = 1L;
        MovieRequestDto requestDto = new MovieRequestDto("New Title", "Director", 2022);

        Movie existingMovie = new Movie();
        existingMovie.setId(id);
        existingMovie.setTitle("Old Title"); // Различно заглавие

        when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toResponseDto(existingMovie)).thenReturn(new MovieResponseDto(id, "New Title", "Director", 2022, null));

        movieService.updateMovie(id, requestDto);

        verify(ratingEnrichmentService).enrich(id, "New Title"); // Трябва да се извика
    }

    @Test
    void updateMovie_WhenTitleSame_ShouldNotTriggerEnrichment() {
        Long id = 1L;
        String title = "Same Title";
        MovieRequestDto requestDto = new MovieRequestDto(title, "New Director", 2022);

        Movie existingMovie = new Movie();
        existingMovie.setId(id);
        existingMovie.setTitle(title);

        when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toResponseDto(existingMovie)).thenReturn(new MovieResponseDto());

        movieService.updateMovie(id, requestDto);

        verify(ratingEnrichmentService, never()).enrich(any(), any());
    }

    @Test
    void updateMovie_WhenNotFound_ShouldThrowException() {
        when(movieRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> movieService.updateMovie(1L, new MovieRequestDto()));
    }

    @Test
    void deleteMovie_WhenExists_ShouldDelete() {
        Long id = 1L;
        when(movieRepository.existsById(id)).thenReturn(true);

        movieService.deleteMovie(id);

        verify(movieRepository).deleteById(id);
    }

    @Test
    void deleteMovie_WhenNotExists_ShouldThrowException() {
        Long id = 1L;
        when(movieRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> movieService.deleteMovie(id));
        verify(movieRepository, never()).deleteById(any());
    }
}