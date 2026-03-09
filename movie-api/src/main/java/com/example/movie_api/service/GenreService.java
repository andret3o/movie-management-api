package com.example.movie_api.service;

import com.example.movie_api.dto.GenreDTO;
import com.example.movie_api.dto.GenreResponseDTO;
import com.example.movie_api.entities.Genre;
import com.example.movie_api.entities.Movie;
import com.example.movie_api.exception.ResourceNotFoundException;
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
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    public GenreResponseDTO create(Genre genre) {
        return toGenreResponseDTO(genreRepository.save(genre));
    }

    public Page<GenreResponseDTO> getAll(Pageable pageable) {
        return genreRepository.findAll(pageable).map(this::toGenreResponseDTO);
    }

    public GenreResponseDTO getById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        return toGenreResponseDTO(genre);
    }

    public GenreResponseDTO update(Long id, GenreDTO genreDTO) {
        Genre genre = getGenreById(id);
        if (genreDTO.getName() != null) {
            genre.setName(genreDTO.getName());
        }
        return toGenreResponseDTO(genreRepository.save(genre));
    }

    public void delete(Long id, boolean force) {
        Genre genre = getGenreById(id);
        if (!force && !genre.getMovies().isEmpty()) {
            throw new IllegalStateException("Cannot delete genre '" + genre.getName() + "' because it has " + genre.getMovies().size() + " associated movies");
        }
        if (force) {
            for (Movie movie : genre.getMovies()) {
                movie.getGenres().remove(genre);
                movieRepository.save(movie);
            }
        }
        genreRepository.delete(genre);
    }

    public Set<Movie> getMoviesForGenre(Long id) {
        return getGenreById(id).getMovies();
    }

    private GenreResponseDTO toGenreResponseDTO(Genre genre){
        GenreResponseDTO dto = new GenreResponseDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setMovieIds(genre.getMovies().stream().map(Movie::getId).sorted().collect(Collectors.toList()));
        return dto;
    }

    private Genre getGenreById(Long id){
        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
    }
}
