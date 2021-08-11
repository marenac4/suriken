package com.videoclub.suriken.api;

import com.videoclub.suriken.jwt.AuthenticationRequest;
import com.videoclub.suriken.jwt.AuthenticationResponse;
import com.videoclub.suriken.model.Genre;
import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.MovieRepository;
import com.videoclub.suriken.repository.RenterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    void auth() {
        HttpEntity<AuthenticationRequest> request = new HttpEntity<>(new AuthenticationRequest("marenac", "cetvorka"));
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity("/authenticate", request, AuthenticationResponse.class);

        jwtToken = response.getBody().getJwt();
    }

    @AfterEach
    void cleanDataBase() {
        movieRepository.deleteAll();
        renterRepository.deleteAll();
    }

    @Test
    void successfullyAddMovie() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Movie> request = new HttpEntity<>(new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3), headers);
        ResponseEntity response = restTemplate.postForEntity("/api/v1/movie", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Optional<Movie> respMovie = movieRepository.findMovieByName("Titanik");
        assertThat(respMovie.isPresent()).isEqualTo(true);
    }

    @Test
    void getAMovieWhenExist() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Cameron", 3);
        movieRepository.save(movie);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Movie> response = restTemplate.exchange("/api/v1/movie/{id}", HttpMethod.GET, request, Movie.class, movie.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Titanik");
        assertThat(response.getBody().getYear()).isEqualTo(1995);
        assertThat(response.getBody().getGenre()).isEqualTo(Genre.DRAMA);
        assertThat(response.getBody().getDirector()).isEqualTo("James Cameron");
        assertThat(response.getBody().getStock()).isEqualTo(3);
    }

    @Test
    void getAMovieWhenNotExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Movie> response = restTemplate.exchange("/api/v1/movie/1", HttpMethod.GET, request, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllMovies() {
        Movie movie1 = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Cameron", 3);
        Movie movie2 = new Movie(null, "The Shawshank Redemption", 1994, Genre.DRAMA, "Frank Darabont", 5);
        Movie movie3 = new Movie(null, "The Godfather", 1972, Genre.CRIME, "Francis Ford Coppola", 1);
        Movie movie4 = new Movie(null, "The Dark Knight", 2008, Genre.ACTION, "Christopher Nolan", 7);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4);
        movieRepository.saveAll(movies);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Movie[]> response = restTemplate.exchange("/api/v1/movie", HttpMethod.GET, request, Movie[].class);

        List<Movie> resMovies = Arrays.asList(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AtomicInteger ind = new AtomicInteger();
        for (Movie mov : movies) {
            assertThat(resMovies.get(ind.get()).getName()).isEqualTo(mov.getName());
            assertThat(resMovies.get(ind.get()).getGenre()).isEqualTo(mov.getGenre());
            assertThat(resMovies.get(ind.get()).getDirector()).isEqualTo(mov.getDirector());
            assertThat(resMovies.get(ind.get()).getStock()).isEqualTo(mov.getStock());
            assertThat(resMovies.get(ind.getAndIncrement()).getYear()).isEqualTo(mov.getYear());
        }
    }

    @Test
    void successfullyRentAMovie() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
        movieRepository.save(movie);
        MovieRenter renter = new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com");
        renterRepository.save(renter);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<?> response = restTemplate.exchange("/api/v1/movie/{id}?renterId={renterId}", HttpMethod.PUT, request, Void.class, movie.getId(), renter.getId());

        Optional<Movie> fetchedMovie = movieRepository.findMovieByIdAndTheirRenters(movie.getId());
        Optional<MovieRenter> fetchedRenter = renterRepository.findRenterByIdAndRentedMovies(renter.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetchedMovie.get().getRenters().get(0).getFirstName()).isEqualTo("Marko");
        assertThat(fetchedRenter.get().getRentedMovies().get(0).getName()).isEqualTo("Titanik");
        assertThat(fetchedMovie.get().getStock()).isEqualTo(2);
    }

    @Test
    void tryRentAMovie_noMovieInStock() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 0);
        movieRepository.save(movie);
        MovieRenter renter = new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com");
        renterRepository.save(renter);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<?> response = restTemplate.exchange("/api/v1/movie/{id}?renterId={renterId}", HttpMethod.PUT, request, Void.class, movie.getId(), renter.getId());

        Optional<Movie> fetchedMovie = movieRepository.findMovieByIdAndTheirRenters(1L);
        Optional<MovieRenter> fetchedRenter = renterRepository.findRenterByIdAndRentedMovies(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(fetchedMovie.get().getRenters().size()).isEqualTo(0);
        assertThat(fetchedRenter.get().getRentedMovies().size()).isEqualTo(0);
    }

    @Test
    void successfullyReturnAMovie() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
        MovieRenter renter = new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com");

        movie.getRenters().add(renter);
        movieRepository.save(movie);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<?> response = restTemplate.exchange("/api/v1/movie/return/{id}?renterId={renterId}", HttpMethod.PUT, request, Void.class, movie.getId(), renter.getId());

        Optional<Movie> fetchedMovie = movieRepository.findMovieByIdAndTheirRenters(movie.getId());
        Optional<MovieRenter> fetchedRenter = renterRepository.findRenterByIdAndRentedMovies(renter.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetchedMovie.get().getRenters().size()).isEqualTo(0);
        assertThat(fetchedRenter.get().getRentedMovies().size()).isEqualTo(0);
    }
}
