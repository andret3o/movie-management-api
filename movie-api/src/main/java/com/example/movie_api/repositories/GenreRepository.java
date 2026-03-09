package com.example.movie_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie_api.entities.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
}
