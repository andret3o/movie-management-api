package com.example.movie_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.repositories.ActorRepository;
import com.example.movie_api.repositories.GenreRepository;
import com.example.movie_api.repositories.MovieRepository;

@RestController
@RequestMapping("/api/clear")
public class ClearController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @DeleteMapping
    public ResponseEntity<String> clearDatabase() {
        // Delete in order: Movies first (they own the join tables), then Actors, then Genres
        movieRepository.deleteAll();
        actorRepository.deleteAll();
        genreRepository.deleteAll();
        
        return ResponseEntity.ok("Database cleared successfully");
    }
}

