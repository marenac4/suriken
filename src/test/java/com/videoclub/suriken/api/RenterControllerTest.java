package com.videoclub.suriken.api;

import com.videoclub.suriken.jwt.AuthenticationRequest;
import com.videoclub.suriken.jwt.AuthenticationResponse;
import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.RenterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RenterControllerTest {

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
        renterRepository.deleteAll();
    }

    @Test
    void successfullyAddRenter() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<MovieRenter> request = new HttpEntity<>(new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com"), headers);

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

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<MovieRenter[]> response = restTemplate.exchange("/api/v1/renter", HttpMethod.GET, request, MovieRenter[].class);

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

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<MovieRenter> response = restTemplate.exchange("/api/v1/renter/{id}", HttpMethod.GET, request, MovieRenter.class, renter.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMail()).isEqualTo("ml@gmail.com");
    }

    @Test
    void badRequestWhenThereIsNoRenter() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<MovieRenter> response = restTemplate.exchange("/api/v1/renter/1", HttpMethod.GET, request, MovieRenter.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
