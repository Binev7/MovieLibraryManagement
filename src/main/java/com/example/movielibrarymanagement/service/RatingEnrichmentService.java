package com.example.movielibrarymanagement.service;

import com.example.movielibrarymanagement.exception.ExternalServiceException;
import com.example.movielibrarymanagement.helper.mapper.RatingMapper;
import com.example.movielibrarymanagement.integration.OmdbClient;
import com.example.movielibrarymanagement.model.Rating;
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
    private final RatingMapper ratingMapper;

    @Async
    @Transactional
    public void enrich(Long movieId, String title) {
        log.info("Starting async enrichment for movie ID: {}", movieId);

        try {
            String ratingValue = omdbClient.fetchImdbRating(title);

            movieRepository.findById(movieId).ifPresent(movie -> {
                Rating rating = movie.getRating();

                if (rating == null) {
                    rating = ratingMapper.toEntity(ratingValue);
                    rating.setMovie(movie);
                    movie.setRating(rating);
                } else {
                    rating.setValue(ratingValue != null ? ratingValue : "N/A");
                }

                movieRepository.save(movie);
                log.info("Enriched movie ID: {} with rating: {}", movieId, rating.getValue());
            });

        } catch (ExternalServiceException e) {
            log.error("Failed to enrich movie ID: {}. Reason: {}", movieId, e.getMessage());

            movieRepository.findById(movieId).ifPresent(movie -> {
                Rating rating = movie.getRating();

                if (rating == null) {
                    rating = ratingMapper.toEntity(null);
                    rating.setMovie(movie);
                    movie.setRating(rating);
                } else {
                    rating.setValue("N/A");
                }

                movieRepository.save(movie);
                log.info("Set N/A rating for movie ID: {} due to enrichment failure", movieId);
            });
        }
    }
}
