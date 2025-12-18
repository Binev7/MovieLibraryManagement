-- =====================================================
-- Movie Library Management System - Sample Data
-- =====================================================
USE movie_library;
-- =====================================================
-- Insert Users
-- =====================================================
-- Note: Passwords are BCrypt hashed for "password123"
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- =====================================================

INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN'),
('john_doe', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_USER'),
('jane_smith', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_USER');

-- =====================================================
-- Insert Movies
-- =====================================================

INSERT INTO movies (title, director, release_year) VALUES
('The Shawshank Redemption', 'Frank Darabont', 1994),
('The Godfather', 'Francis Ford Coppola', 1972),
('The Dark Knight', 'Christopher Nolan', 2008),
('Inception', 'Christopher Nolan', 2010),
('Pulp Fiction', 'Quentin Tarantino', 1994),
('Forrest Gump', 'Robert Zemeckis', 1994),
('The Matrix', 'The Wachowskis', 1999),
('Goodfellas', 'Martin Scorsese', 1990),
('The Lord of the Rings: The Return of the King', 'Peter Jackson', 2003),
('Fight Club', 'David Fincher', 1999),
('Interstellar', 'Christopher Nolan', 2014),
('The Silence of the Lambs', 'Jonathan Demme', 1991),
('Saving Private Ryan', 'Steven Spielberg', 1998),
('The Green Mile', 'Frank Darabont', 1999),
('Schindler''s List', 'Steven Spielberg', 1993);

-- =====================================================
-- Insert Ratings
-- =====================================================
-- Note: These are sample ratings. In production, ratings
-- are fetched asynchronously from OMDB API
-- =====================================================

INSERT INTO ratings (value, movie_id) VALUES
('9.3', 1),  -- The Shawshank Redemption
('9.2', 2),  -- The Godfather
('9.0', 3),  -- The Dark Knight
('8.8', 4),  -- Inception
('8.9', 5),  -- Pulp Fiction
('8.8', 6),  -- Forrest Gump
('8.7', 7),  -- The Matrix
('8.7', 8),  -- Goodfellas
('9.0', 9),  -- The Lord of the Rings
('8.8', 10), -- Fight Club
('8.7', 11), -- Interstellar
('8.6', 12), -- The Silence of the Lambs
('8.6', 13), -- Saving Private Ryan
('8.6', 14), -- The Green Mile
('9.0', 15); -- Schindler's List

-- =====================================================


