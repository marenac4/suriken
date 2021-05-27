package com.videoclub.suriken.api;

import com.videoclub.suriken.model.Genre;
import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.MovieRepository;
import com.videoclub.suriken.repository.RenterRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    Logger logger = LoggerFactory.getLogger(MovieControllerTest.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private TestRestTemplate restTemplate;

//    @BeforeAll
//    static void beforeAll() {
//        Movie movie1 = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
//        Movie movie2 = new Movie(null, "The Shawshank Redemption", 1994, Genre.DRAMA, "Frank Darabont", 5);
//        Movie movie3 = new Movie(null, "The Godfather", 1972, Genre.CRIME, "Francis Ford Coppola", 1);
//        Movie movie4 = new Movie(null, "The Dark Knight", 2008, Genre.ACTION, "Christopher Nolan", 7);
//    }

    @Test
    void successfullyAddMovie() {
        HttpEntity<Movie> request = new HttpEntity<>(new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3));
        ResponseEntity response = restTemplate.postForEntity("/api/v1/movie", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Optional<Movie> respMovie = movieRepository.findMovieByName("Titanik");
//        logger.info("Id of saved movie is {}", respMovie.get().getId());
        assertThat(respMovie.isPresent()).isEqualTo(true);
    }

    @Test
    void getAMovieWhenExist() {
        movieRepository.save(new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3));

        ResponseEntity<Movie> response = restTemplate.getForEntity("/api/v1/movie/1", Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAMovieWhenNotExist() {
        ResponseEntity<Movie> response = restTemplate.getForEntity("/api/v1/movie/1", Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllMovies() {
        Movie movie1 = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
        Movie movie2 = new Movie(null, "The Shawshank Redemption", 1994, Genre.DRAMA, "Frank Darabont", 5);
        Movie movie3 = new Movie(null, "The Godfather", 1972, Genre.CRIME, "Francis Ford Coppola", 1);
        Movie movie4 = new Movie(null, "The Dark Knight", 2008, Genre.ACTION, "Christopher Nolan", 7);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4);
        movieRepository.saveAll(movies);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity("/api/v1/movie", Movie[].class);

        List<Movie> resMovies = Arrays.asList(response.getBody());

        resMovies.forEach(movie -> logger.debug("Movie name : {}", movie.getName()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AtomicInteger ind = new AtomicInteger();
        for (Movie mov : movies) {
            assertThat(resMovies.get(ind.getAndIncrement()).getName()).isEqualTo(mov.getName());
        }
    }

    @Test
    void successfullyRentAMovie() {
        movieRepository.save(new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3));
        renterRepository.save(new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com"));

        HttpEntity<?> req = HttpEntity.EMPTY;
        ResponseEntity<?> response = restTemplate.exchange("/api/v1/movie/{id}?renterId={renterId}", HttpMethod.PUT, req, Void.class, 1,1);

        Optional<Movie> fetchedMovie = movieRepository.findMovieByIdAndTheirRenters(1L);
        Optional<MovieRenter> fetchedRenter = renterRepository.findRenterByIdAndRentedMovies(1L);

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

        logger.info("movie id: {} renter id: {}", movie.getId(), renter.getId());

        HttpEntity<?> req = HttpEntity.EMPTY;
        ResponseEntity<?> response = restTemplate.exchange("/api/v1/movie/{id}?renterId={renterId}", HttpMethod.PUT, req, Void.class, 1,1);

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

        HttpEntity<?> req = HttpEntity.EMPTY;
        ResponseEntity<?> response = restTemplate.exchange("/api/v1/movie/return/{id}?renterId={renterId}", HttpMethod.PUT, req, Void.class, 1,1);

        Optional<Movie> fetchedMovie = movieRepository.findMovieByIdAndTheirRenters(1L);
        Optional<MovieRenter> fetchedRenter = renterRepository.findRenterByIdAndRentedMovies(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetchedMovie.get().getRenters().size()).isEqualTo(0);
        assertThat(fetchedRenter.get().getRentedMovies().size()).isEqualTo(0);
    }
}
