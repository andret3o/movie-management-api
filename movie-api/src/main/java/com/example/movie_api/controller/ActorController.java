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

import com.example.movie_api.dto.ActorDTO;
import com.example.movie_api.dto.ActorResponseDTO;
import com.example.movie_api.dto.MovieResponseDTO;
import com.example.movie_api.entities.Actor;
import com.example.movie_api.entities.Movie;
import com.example.movie_api.service.ActorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    
    @Autowired
    private ActorService actorService;

    @PostMapping
    public ResponseEntity<ActorResponseDTO> create(@Valid @RequestBody ActorDTO actorDTO) {
        return ResponseEntity.status(201).body(actorService.create(actorDTO));
    }

    @GetMapping
    public Page<ActorResponseDTO> getAll(@RequestParam(required = false) String name,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (name != null) {
            return actorService.getByName(name, pageable);
        }
        return actorService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(actorService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ActorDTO actorDTO){
        return ResponseEntity.ok(actorService.update(id, actorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force){
        actorService.delete(id, force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/movies")
    public List<MovieResponseDTO> getMoviesForActor(@PathVariable Long id){
        Set<Movie> movies = actorService.getMoviesForActor(id);

        return movies.stream()
                .map(movie -> {
                    MovieResponseDTO dto = new MovieResponseDTO();
                    dto.setId(movie.getId());
                    dto.setTitle(movie.getTitle());
                    dto.setReleaseYear(movie.getReleaseYear());
                    dto.setDuration(movie.getDuration());
                    dto.setActorIds(movie.getActors().stream().map(Actor::getId).sorted().collect(Collectors.toList()));
                    return dto;
                }).toList();
    }
}
