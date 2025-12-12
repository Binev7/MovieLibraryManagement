package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.exception.ExternalServiceException;
import com.example.movielibrarymanagement.integration.OmdbClient;
import com.example.movielibrarymanagement.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingEnrichmentService {

    private final OmdbClient omdbClient;
    private final MovieRepository movieRepository;

    @Async
    @Transactional
    public void enrich(Long movieId, String title) {
        log.info("Starting async enrichment for movie ID: {}", movieId);

        try {
            Double rating = omdbClient.fetchImdbRating(title);

            if (rating != null) {
                movieRepository.findById(movieId).ifPresent(movie -> {
                    movie.setRating(rating);
                    movieRepository.save(movie);
                    log.info("Enriched movie ID: {} with rating: {}", movieId, rating);
                });
            } else {
                log.info("No rating found for movie: {}", title);
            }

        } catch (ExternalServiceException e) {
            log.error("Failed to enrich movie ID: {}. Reason: {}", movieId, e.getMessage());
        }
    }
}
