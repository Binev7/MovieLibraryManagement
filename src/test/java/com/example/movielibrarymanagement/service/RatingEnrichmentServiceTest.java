package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.helper.mapper.RatingMapper;
import com.example.movielibrarymanagement.integration.OmdbClient;
import com.example.movielibrarymanagement.model.Movie;
import com.example.movielibrarymanagement.model.Rating;
import com.example.movielibrarymanagement.repository.MovieRepository;
import com.example.movielibrarymanagement.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingEnrichmentServiceTest {

    @Mock
    private OmdbClient omdbClient;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingEnrichmentService ratingEnrichmentService;

    @Test
    void enrich_WhenRatingFound_ShouldUpdateMovie() {
        Long movieId = 1L;
        String title = "Inception";
        String ratingValue = "8.8";
        Movie movie = new Movie();
        movie.setId(movieId);

        Rating rating = new Rating();
        rating.setValue("8.8");

        when(omdbClient.fetchImdbRating(title)).thenReturn(ratingValue);
        when(ratingMapper.toEntity(ratingValue)).thenReturn(rating);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        ratingEnrichmentService.enrich(movieId, title);

        verify(movieRepository).save(movie);
        assertNotNull(movie.getRating());
        assertEquals("8.8", movie.getRating().getValue());
    }

    @Test
    void enrich_WhenRatingNotFound_ShouldSetNA() {
        Long movieId = 1L;
        String title = "Unknown Movie";
        Movie movie = new Movie();
        movie.setId(movieId);

        Rating rating = new Rating();
        rating.setValue("N/A");

        when(omdbClient.fetchImdbRating(title)).thenReturn(null);
        when(ratingMapper.toEntity(null)).thenReturn(rating);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        ratingEnrichmentService.enrich(movieId, title);

        verify(movieRepository).save(movie);
        assertNotNull(movie.getRating());
        assertEquals("N/A", movie.getRating().getValue());
    }

    @Test
    void enrich_WhenMovieDeletedMeanwhile_ShouldNotUpdate() {
        Rating rating = new Rating();
        rating.setValue("8.0");

        when(omdbClient.fetchImdbRating(any())).thenReturn("8.0");
        when(ratingMapper.toEntity("8.0")).thenReturn(rating);
        when(movieRepository.findById(any())).thenReturn(Optional.empty());

        ratingEnrichmentService.enrich(1L, "Title");

        verify(movieRepository, never()).save(any());
    }
}