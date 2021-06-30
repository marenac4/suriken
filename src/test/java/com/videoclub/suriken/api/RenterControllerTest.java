package com.videoclub.suriken.api;

import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.RenterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RenterControllerTest {

    Logger logger = LoggerFactory.getLogger(MovieControllerTest.class);

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void successfullyAddRenter() {
        HttpEntity<MovieRenter> request = new HttpEntity<>(new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com"));

        ResponseEntity response = restTemplate.postForEntity("/api/v1/renter", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Optional<MovieRenter> fetchedRenter = renterRepository.findMovieRenterByMail("marko@gmail.com");
        assertThat(fetchedRenter.isPresent()).isEqualTo(true);
    }

    @Test
    void getAllRenters() {
        MovieRenter renter1 = new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com");
        MovieRenter renter2 = new MovieRenter(null, "Dusan", "Milanovic", "dm@gmail.com");
        MovieRenter renter3 = new MovieRenter(null, "Djina", "Simic", "dj@live.com");

        List<MovieRenter> renters = Arrays.asList(renter1, renter2, renter3);
        renterRepository.saveAll(renters);

        ResponseEntity<MovieRenter[]> response = restTemplate.getForEntity("/api/v1/renter", MovieRenter[].class);

        List<MovieRenter> rentersFromResponse = Arrays.asList(response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Integer ind = 0;
        for (MovieRenter renter : renters) {
            assertThat(rentersFromResponse.get(ind++).getMail()).isEqualTo(renter.getMail());
        }
    }

    @Test
    void getRenterWhenExists() {
        MovieRenter renter = new MovieRenter(null, "Marko", "Lazovic", "ml@gmail.com");
        renterRepository.save(renter);

        ResponseEntity<MovieRenter> response = restTemplate.getForEntity("/api/v1/renter/1", MovieRenter.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMail()).isEqualTo("ml@gmail.com");
    }

    @Test
    void badRequestWhenThereIsNoRenter() {
        ResponseEntity<MovieRenter> response = restTemplate.getForEntity("/api/v1/renter/1", MovieRenter.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
