package com.example.movie_api.dto;

import java.util.List;

public class MovieResponseDTO {
    private Long id;
    private String title;
    private int releaseYear;
    private int duration;
    private List<Long> genreIds;
    private List<Long> actorIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Long> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<Long> actorIds) {
        this.actorIds = actorIds;
    }
}