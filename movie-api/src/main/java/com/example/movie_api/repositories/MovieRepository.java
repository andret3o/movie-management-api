package com.example.movie_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.movie_api.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Movie> findByReleaseYear(int year, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    Page<Movie> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
    Page<Movie> findByActorId(@Param("actorId") Long actorId, Pageable pageable);
}
