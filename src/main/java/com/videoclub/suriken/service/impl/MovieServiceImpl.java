package com.videoclub.suriken.service.impl;

import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.MovieRepository;
import com.videoclub.suriken.repository.RenterRepository;
import com.videoclub.suriken.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;
    private final RenterRepository renterRepository;

    public MovieServiceImpl(MovieRepository movieRepository, RenterRepository renterRepository) {
        this.movieRepository = movieRepository;
        this.renterRepository = renterRepository;
    }

    @Override
    public void addMovieToDb(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        movies.forEach(movie -> logger.debug("Movie" + movie.getName() + "has :" + movie.getRenters().size()));

        return movies;
    }

    @Override
    public String rentAMovie(Long movieId, MovieRenter movieRenter) {
        Movie movieToRent = movieRepository.findById(movieId)
                .orElseThrow();

        if (movieToRent.decrementStockValueByOne()) {
            movieToRent.addMovieRenter(movieRenter);
            movieRepository.save(movieToRent);
            return "Movie successfully rented!";
        }
        else {
            return "Sorry! There is no movie in the stock.";
        }
    }

    @Override
    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow();
    }

    @Override
    public String returnAMovie(Long movieId, MovieRenter movieRenter) {
        Movie movieToReturn = movieRepository.findById(movieId)
                .orElseThrow();

        movieRenter = renterRepository.findById(movieRenter.getId())
                .orElseThrow();

        movieToReturn.incrementStockByOne();
        movieToReturn.removeMovieRenter(movieRenter);
        movieRepository.save(movieToReturn);

        return "Movie successfully returned!";
    }
}
