package com.example.movielibrarymanagement.helper.mapper;

import com.example.movielibrarymanagement.model.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public Rating toEntity(String value) {
        Rating rating = new Rating();
        rating.setValue(value != null ? value : "N/A");
        return rating;
    }

    public String toValueString(Rating rating) {
        return rating != null ? rating.getValue() : "N/A";
    }
}
