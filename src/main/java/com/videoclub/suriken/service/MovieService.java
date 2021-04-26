package com.videoclub.suriken.service;

import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;

import java.util.List;

public interface MovieService {

    void addMovieToDb (Movie movie);

    List<Movie> getAllMovies();

    void rentAMovie(Long movieId, Long renterId);

    Movie getMovie(Long movieId);

    void returnAMovie(Long movieId, Long renterId);
}
