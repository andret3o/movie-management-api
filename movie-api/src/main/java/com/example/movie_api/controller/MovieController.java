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

import com.example.movie_api.dto.ActorResponseDTO;
import com.example.movie_api.dto.MovieDTO;
import com.example.movie_api.dto.MovieResponseDTO;
import com.example.movie_api.entities.Actor;
import com.example.movie_api.entities.Movie;
import com.example.movie_api.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    public MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponseDTO> create(@Valid @RequestBody MovieDTO movie){
        return ResponseEntity.status(201).body(movieService.create(movie));
    }

    @GetMapping
    public Page<MovieResponseDTO> getAll(@RequestParam(required = false) Long genre,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(required = false) Long actor,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size){
        
        Pageable pageable = PageRequest.of(page, size);
        if(genre != null){
            return movieService.getByGenre(genre, pageable);
        }else if(year != null){
            return movieService.getByYear(year, pageable);
        }else if(actor != null){
            return movieService.getByActor(actor, pageable);
        }
        return movieService.getAll(pageable);
    }

    @GetMapping("/search")
    public Page<MovieResponseDTO> searchByTitle(@RequestParam String title,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size){
    Pageable pageable = PageRequest.of(page, size);
    return movieService.searchByTitle(title, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO){
        return ResponseEntity.ok(movieService.update(id, movieDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force){
        movieService.delete(id, force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/actors")
    public List<ActorResponseDTO> getActorsForMovie(@PathVariable Long id){
        Set<Actor> actors = movieService.getActorsForMovie(id);
        return actors.stream()
                .map(actor -> {
                    ActorResponseDTO dto = new ActorResponseDTO();
                    dto.setId(actor.getId());
                    dto.setName(actor.getName());
                    dto.setBirthDate(actor.getBirthDate());
                    dto.setMovieIds(actor.getMovies().stream().map(Movie::getId).sorted().collect(Collectors.toList()));
                    return dto;
                })
                .toList();
    }
}
