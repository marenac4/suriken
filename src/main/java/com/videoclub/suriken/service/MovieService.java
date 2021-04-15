package com.videoclub.suriken.service;

import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;

import java.util.List;

public interface MovieService {

    void addMovieToDb (Movie movie);

    List<Movie> getAllMovies();

    String rentAMovie(Long movieId, MovieRenter movieRenter);

    Movie getMovie(Long movieId);

    String returnAMovie(Long movieId, MovieRenter movieRenter);
}
