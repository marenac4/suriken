package com.videoclub.suriken.api;

import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.service.impl.MovieServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity addMovie(@RequestBody Movie movie){
        movieService.addMovieToDb(movie);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable(name = "id") Long movieId) {
        return new ResponseEntity<>(movieService.getMovie(movieId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity rentAMovie(@PathVariable(name = "id") Long movieId, @RequestParam(name = "renterId") Long renterId) {
        movieService.rentAMovie(movieId, renterId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity returnAMovie( @PathVariable(name = "id") Long movieId, @RequestParam(name = "renterId") Long renterId) {
        movieService.returnAMovie(movieId, renterId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
