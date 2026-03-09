package com.example.movie_api.dto;

import jakarta.validation.constraints.Size;

public class GenreDTO {
    @Size(min = 1, message = "Name cannot be empty")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
