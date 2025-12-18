package com.example.movielibrarymanagement.integration;

import com.example.movielibrarymanagement.dto.OmdbResponseDto;
import com.example.movielibrarymanagement.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class OmdbClient {

    @Value("${omdb.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    private static final String OMDB_URL = "http://www.omdbapi.com/";

    public String fetchImdbRating(String title) {
        if (!StringUtils.hasText(apiKey)) {
            throw new ExternalServiceException("OMDb API key is not configured in application.properties");
        }

        try {
            URI uri = UriComponentsBuilder.fromUriString(OMDB_URL)
                    .queryParam("t", title)
                    .queryParam("apikey", apiKey)
                    .build()
                    .toUri();

            log.debug("Calling OMDb for title: {}", title);

            OmdbResponseDto response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(OmdbResponseDto.class);

            if (response != null && "True".equalsIgnoreCase(response.response())) {
                String rating = response.imdbRating();
                return (StringUtils.hasText(rating) && !"N/A".equalsIgnoreCase(rating)) ? rating : null;
            } else {
                log.warn("OMDb returned error for '{}': {}", title, response != null ? response.error() : "Unknown error");
            }

        } catch (Exception e) {
            log.error("Failed to fetch rating for '{}': {}", title, e.getMessage());
        }

        return null;
    }
}