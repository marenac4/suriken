package com.videoclub.suriken.service.impl;

import com.videoclub.suriken.exceptions.NoMovieInStockException;
import com.videoclub.suriken.exceptions.RestException;
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

        movies.forEach(movie -> logger.debug("Movie {} has this number of renters: {}" , movie.getName(), movie.getRenters().size()));

        return movies;
    }

    @Override
    public void rentAMovie(Long movieId, Long renterId) {
        Movie movieToRent = movieRepository.findById(movieId)
                .orElseThrow(() -> new RestException("Exception.movieNotFound", new String[]{movieId.toString()}));

        if (movieToRent.getStock() == 0)
            throw new NoMovieInStockException("Exception.NoMovieInStock", new String[]{movieToRent.getId().toString(), movieToRent.getName()});

        MovieRenter movieRenter = renterRepository.findById(renterId)
                .orElseThrow(() -> new RestException("Exception.renterNotFound", new String[]{renterId.toString()}));

        movieToRent.decrementStockValueByOne();
        movieToRent.addMovieRenter(movieRenter);
        movieRepository.save(movieToRent);
    }

    @Override
    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new RestException("Exception.movieNotFound", new String[]{movieId.toString()}));
    }

    @Override
    public void returnAMovie(Long movieId, Long renterId) {
        Movie movieToReturn = movieRepository.findById(movieId)
                .orElseThrow(() -> new RestException("Exception.movieNotFound", null));

        MovieRenter movieRenter = renterRepository.findById(renterId)
                .orElseThrow(() -> new RestException("Exception.renterNotFound", new String[]{renterId.toString()}));

        movieToReturn.incrementStockByOne();
        movieToReturn.removeMovieRenter(movieRenter);
        movieRepository.save(movieToReturn);
    }
}
