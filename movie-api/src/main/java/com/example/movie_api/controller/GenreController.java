package com.example.movie_api.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie_api.dto.GenreDTO;
import com.example.movie_api.dto.GenreResponseDTO;
import com.example.movie_api.dto.MovieResponseDTO;
import com.example.movie_api.entities.Actor;
import com.example.movie_api.entities.Genre;
import com.example.movie_api.entities.Movie;
import com.example.movie_api.service.GenreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreResponseDTO> create(@Valid @RequestBody GenreDTO genreDTO){
        Genre genre = new Genre(genreDTO.getName());
        return ResponseEntity.status(201).body(genreService.create(genre));
    }

    @GetMapping
    public Page<GenreResponseDTO> getAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return genreService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(genreService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> update(@PathVariable Long id, @Valid @RequestBody GenreDTO genreDTO){
        return ResponseEntity.ok(genreService.update(id, genreDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force){
        genreService.delete(id, force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/movies")
    public List<MovieResponseDTO> getMoviesForGenre(@PathVariable Long id){
        Set<Movie> movies = genreService.getMoviesForGenre(id);
        return movies.stream()
                .map(movie -> {
                    MovieResponseDTO dto = new MovieResponseDTO();
                    dto.setId(movie.getId());
                    dto.setTitle(movie.getTitle());
                    dto.setReleaseYear(movie.getReleaseYear());
                    dto.setDuration(movie.getDuration());
                    dto.setGenreIds(movie.getGenres().stream().map(Genre::getId).sorted().collect(Collectors.toList()));
                    dto.setActorIds(movie.getActors().stream().map(Actor::getId).sorted().collect(Collectors.toList()));
                    return dto;
                }).toList();
    }
}
