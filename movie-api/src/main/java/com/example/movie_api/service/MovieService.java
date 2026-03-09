package com.example.movie_api.service;

import com.example.movie_api.dto.MovieDTO;
import com.example.movie_api.dto.MovieResponseDTO;
import com.example.movie_api.entities.Actor;
import com.example.movie_api.entities.Genre;
import com.example.movie_api.entities.Movie;
import com.example.movie_api.exception.ResourceNotFoundException;
import com.example.movie_api.repositories.ActorRepository;
import com.example.movie_api.repositories.GenreRepository;
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
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    public MovieResponseDTO create(MovieDTO movieDTO) {
        Movie movie = new Movie(movieDTO.getTitle(), movieDTO.getReleaseYear(), movieDTO.getDuration());
        if (movieDTO.getGenreIds() != null) {
            for (Long genreId : movieDTO.getGenreIds()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + genreId));
                movie.getGenres().add(genre);
                genre.getMovies().add(movie);
            }
        }
        if (movieDTO.getActorIds() != null) {
            for (Long actorId : movieDTO.getActorIds()) {
                Actor actor = actorRepository.findById(actorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + actorId));
                movie.getActors().add(actor);
                actor.getMovies().add(movie);
            }
        }
        return toResponseDTO(movieRepository.save(movie));
    }

    public Page<MovieResponseDTO> getAll(Pageable pageable) {
        return movieRepository.findAll(pageable).map(this::toResponseDTO);
    }

    public MovieResponseDTO getById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
        return toResponseDTO(movie);
    }

    public Page<MovieResponseDTO> getByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenreId(genreId, pageable).map(this::toResponseDTO);
    }

    public Page<MovieResponseDTO> getByYear(int year, Pageable pageable) {
        return movieRepository.findByReleaseYear(year, pageable).map(this::toResponseDTO);
    }

    public Page<MovieResponseDTO> getByActor(Long actorId, Pageable pageable) {
        return movieRepository.findByActorId(actorId, pageable).map(this::toResponseDTO);
    }

    public Page<MovieResponseDTO> searchByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable).map(this::toResponseDTO);
    }

    public MovieResponseDTO update(Long id, MovieDTO movieDTO) {
        Movie movie = getMovieById(id);
        if (movieDTO.getTitle() != null) {
            movie.setTitle(movieDTO.getTitle());
        }
        if (movieDTO.getReleaseYear() != null) {
            movie.setReleaseYear(movieDTO.getReleaseYear());
        }
        if (movieDTO.getDuration() != null) {
            movie.setDuration(movieDTO.getDuration());
        }
        if (movieDTO.getGenreIds() != null) {
            movie.getGenres().clear();
            for (Long genreId : movieDTO.getGenreIds()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + genreId));
                movie.getGenres().add(genre);
                genre.getMovies().add(movie);
            }
        }
        if (movieDTO.getActorIds() != null) {
            movie.getActors().clear();
            for (Long actorId : movieDTO.getActorIds()) {
                Actor actor = actorRepository.findById(actorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + actorId));
                movie.getActors().add(actor);
                actor.getMovies().add(movie);
            }
        }
        return toResponseDTO(movieRepository.save(movie));
    }

    public void delete(Long id, boolean force) {
        Movie movie = getMovieById(id);
        if (!force && (!movie.getGenres().isEmpty() || !movie.getActors().isEmpty())) {
            throw new IllegalStateException("Cannot delete movie '" + movie.getTitle() + "' because it has associated genres and/or actors");
        }
        if (force) {
            for (Genre genre : movie.getGenres()) {
                genre.getMovies().remove(movie);
                genreRepository.save(genre);
            }
            for (Actor actor : movie.getActors()) {
                actor.getMovies().remove(movie);
                actorRepository.save(actor);
            }
        }
        movieRepository.delete(movie);
    }

    public Set<Actor> getActorsForMovie(Long id) {
        return getMovieById(id).getActors();
    }

    private MovieResponseDTO toResponseDTO(Movie movie) {
        MovieResponseDTO dto = new MovieResponseDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDuration(movie.getDuration());
        dto.setGenreIds(movie.getGenres().stream().map(Genre::getId).sorted().collect(Collectors.toList()));
        dto.setActorIds(movie.getActors().stream().map(Actor::getId).sorted().collect(Collectors.toList()));
        return dto;
    }

    private Movie getMovieById(Long id) {
    Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    return movie;
}
}
