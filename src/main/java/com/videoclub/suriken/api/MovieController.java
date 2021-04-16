package com.videoclub.suriken.api;

import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.service.impl.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/movie")
@RestController
public class MovieController {

    private final MovieServiceImpl movieService;

    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public void addMovie(@RequestBody Movie movie){
        movieService.addMovieToDb(movie);
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable(name = "id") Long movieId) {
//        return movieService.getMovie(movieId);
        return movieService.getMovie(movieId);
    }

    @PutMapping("/{id}")
    public String rentAMovie(
            @PathVariable(name = "id") Long movieId,
            @RequestBody MovieRenter movieRenter) {
        return movieService.rentAMovie(movieId, movieRenter);
    }

    @PutMapping("/return/{id}")
    public String returnAMovie(
            @PathVariable(name = "id") Long movieId,
            @RequestBody MovieRenter movieRenter) {
        return movieService.returnAMovie(movieId, movieRenter);
    }

}
