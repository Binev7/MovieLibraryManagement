package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.integration.OmdbClient;
import com.example.movielibrarymanagement.model.Movie;
import com.example.movielibrarymanagement.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingEnrichmentServiceTest {

    @Mock
    private OmdbClient omdbClient;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RatingEnrichmentService ratingEnrichmentService;

    @Test
    void enrich_WhenRatingFound_ShouldUpdateMovie() {
        Long movieId = 1L;
        String title = "Inception";
        Double rating = 8.8;
        Movie movie = new Movie();

        when(omdbClient.fetchImdbRating(title)).thenReturn(rating);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        ratingEnrichmentService.enrich(movieId, title);

        verify(movieRepository).save(movie);
        assert(movie.getRating()).equals(rating);
    }

    @Test
    void enrich_WhenRatingNotFound_ShouldNotUpdate() {
        when(omdbClient.fetchImdbRating(any())).thenReturn(null);

        ratingEnrichmentService.enrich(1L, "Unknown Movie");

        verify(movieRepository, never()).findById(any());
        verify(movieRepository, never()).save(any());
    }

    @Test
    void enrich_WhenMovieDeletedMeanwhile_ShouldNotUpdate() {
        when(omdbClient.fetchImdbRating(any())).thenReturn(8.0);
        when(movieRepository.findById(any())).thenReturn(Optional.empty());

        ratingEnrichmentService.enrich(1L, "Title");

        verify(movieRepository, never()).save(any());
    }
}