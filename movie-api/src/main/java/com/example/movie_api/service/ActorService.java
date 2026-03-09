package com.example.movie_api.service;

import com.example.movie_api.dto.ActorDTO;
import com.example.movie_api.dto.ActorResponseDTO;
import com.example.movie_api.entities.Actor;
import com.example.movie_api.entities.Movie;
import com.example.movie_api.exception.ResourceNotFoundException;
import com.example.movie_api.repositories.ActorRepository;
import com.example.movie_api.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ActorResponseDTO create(ActorDTO actorDTO) {
        Actor actor = new Actor(actorDTO.getName(), actorDTO.getBirthDate());
        if (actorDTO.getMovieIds() != null) {
            for (Long movieId : actorDTO.getMovieIds()) {
                Movie movie = movieRepository.findById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
                actor.getMovies().add(movie);
                movie.getActors().add(actor);
            }
        }
        return toResponseDTO(actorRepository.save(actor));
    }

    public Page<ActorResponseDTO> getAll(Pageable pageable) {
        return actorRepository.findAll(pageable).map(this::toResponseDTO);
    }

    public ActorResponseDTO getById(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
        return toResponseDTO(actor);
    }

    public Page<ActorResponseDTO> getByName(String name, Pageable pageable) {
        return actorRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toResponseDTO);
    }

    public ActorResponseDTO update(Long id, ActorDTO actorDTO) {
        Actor actor = getActorById(id);
        if (actorDTO.getName() != null) {
            actor.setName(actorDTO.getName());
        }
        if (actorDTO.getBirthDate() != null) {
            actor.setBirthDate(actorDTO.getBirthDate());
        }
        if (actorDTO.getMovieIds() != null) {
            actor.getMovies().clear();
            for (Long movieId : actorDTO.getMovieIds()) {
                Movie movie = movieRepository.findById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
                actor.getMovies().add(movie);
                movie.getActors().add(actor);
            }
        }
        return toResponseDTO(actorRepository.save(actor));
    }

    public void delete(Long id, boolean force) {
        Actor actor = getActorById(id);
        if (!force && !actor.getMovies().isEmpty()) {
            throw new IllegalStateException("Unable to delete actor '" + actor.getName() + "' as they are associated with " + actor.getMovies().size() + " movies");
        }
        if (force) {
            for (Movie movie : actor.getMovies()) {
                movie.getActors().remove(actor);
                movieRepository.save(movie);
            }
        }
        actorRepository.delete(actor);
    }

    public Set<Movie> getMoviesForActor(Long id) {
        return getActorById(id).getMovies();
    }

    private ActorResponseDTO toResponseDTO(Actor actor){
        ActorResponseDTO dto = new ActorResponseDTO();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        dto.setBirthDate(actor.getBirthDate());
        dto.setMovieIds(actor.getMovies().stream().map(Movie::getId).sorted().collect(Collectors.toList()));
        return dto;
    }

    private Actor getActorById(Long id){
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    }
}
