package com.example.movie_api.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();

    public Genre(){}

    public Genre(String name){
        this.name = name;
    }

    public Long getId(){return id;};
    public void setId(Long id){this.id = id;};
    public String getName(){return name;};
    public void setName(String name){this.name = name;};
    public Set<Movie> getMovies(){return movies;};
    public void setMovies(Set<Movie> movies){this.movies = movies;};
}
