-- =====================================================
-- Movie Library Management System - Database Schema
-- =====================================================

-- Drop database if it exists to start fresh
DROP DATABASE IF EXISTS movie_library;
CREATE DATABASE movie_library;;
USE movie_library;


CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT chk_role CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Index for faster username lookups during authentication
CREATE INDEX idx_users_username ON users(username);


CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255),
    release_year INT,
    CONSTRAINT chk_release_year CHECK (release_year >= 1888 AND release_year <= 2100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Index for faster title searches
CREATE INDEX idx_movies_title ON movies(title);
CREATE INDEX idx_movies_director ON movies(director);
CREATE INDEX idx_movies_release_year ON movies(release_year);


CREATE TABLE ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    value VARCHAR(50) NOT NULL,
    movie_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_ratings_movie FOREIGN KEY (movie_id)
        REFERENCES movies(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Index for faster rating lookups by movie
CREATE INDEX idx_ratings_movie_id ON ratings(movie_id);

-- =====================================================

