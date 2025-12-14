package com.example.movielibrarymanagement.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;

@RestController
@Hidden
public class HomeController {

    @GetMapping("/")
    public RedirectView handleRootRequest(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return new RedirectView("/swagger-ui/index.html");
        } else {
            return new RedirectView("/api/movies");
        }
    }
}