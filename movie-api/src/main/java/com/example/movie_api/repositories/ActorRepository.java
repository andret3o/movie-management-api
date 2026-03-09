package com.example.movie_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie_api.entities.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
