package com.example.movielibrarymanagement.helper.parser;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Parser for handling rating values from external APIs (e.g., OMDB).
 * Responsible for validating and normalizing rating strings.
 */
@Component
public class RatingParser {

    /**
     * Parses a rating string from an external API response.
     * Returns null if the rating is "N/A", empty, or invalid.
     *
     * @param ratingString the rating string from the API (e.g., "7.5", "N/A")
     * @return the normalized rating string, or null if not available
     */
    public String parseRating(String ratingString) {
        if (!StringUtils.hasText(ratingString)) {
            return null;
        }

        if ("N/A".equalsIgnoreCase(ratingString.trim())) {
            return null;
        }

        return ratingString.trim();
    }
}

