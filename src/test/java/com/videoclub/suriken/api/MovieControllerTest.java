package com.videoclub.suriken.api;

import com.videoclub.suriken.model.Genre;
import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    Logger logger = LoggerFactory.getLogger(MovieControllerTest.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void successfullyAddMovie() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);

        HttpEntity<Movie> request = new HttpEntity<>(movie);

        ResponseEntity response = restTemplate.postForEntity("/api/v1/movie", request, Void.class);

        assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());

        Optional<Movie> respMovie = movieRepository.findMovieByName("Titanik");
        logger.info("Id of saved movie is {}", respMovie.get().getId());
        assertThat(true).isEqualTo(respMovie.isPresent());
    }

    @Test
    void canGetAMovieWhenExist() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);

        movieRepository.save(movie);

        ResponseEntity<Movie> response = restTemplate.getForEntity("/api/v1/movie/1", Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}