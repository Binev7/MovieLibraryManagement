package com.example.movielibrarymanagement.helper.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RatingParser {

    public Double parseRating(String rating) {
        if (StringUtils.hasText(rating) && !"N/A".equalsIgnoreCase(rating)) {
            try {
                return Double.parseDouble(rating);
            } catch (NumberFormatException e) {
                log.warn("Invalid rating format received: {}", rating);
            }
        }
        return null;
    }
}
