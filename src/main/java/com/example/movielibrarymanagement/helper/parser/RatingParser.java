package com.example.movielibrarymanagement.helper.parser;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RatingParser {

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

