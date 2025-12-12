package com.example.movielibrarymanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OmdbResponseDto(
        @JsonProperty("Title") String title,
        @JsonProperty("imdbRating") String imdbRating,
        @JsonProperty("Response") String response,
        @JsonProperty("Error") String error
) {}