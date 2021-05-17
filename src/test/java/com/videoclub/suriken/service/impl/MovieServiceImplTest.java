package com.videoclub.suriken.service.impl;

import com.videoclub.suriken.model.Genre;
import com.videoclub.suriken.model.Movie;
import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.MovieRepository;
import com.videoclub.suriken.repository.RenterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RenterRepository renterRepository;

    @InjectMocks
    private MovieServiceImpl underTest;

    @Test
    void rentAMovie() {
        Movie movie = new Movie(1L, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
        MovieRenter renter = new MovieRenter(2L, "Marko", "Lazovic", "marko@gmail.com");

        doReturn(Optional.of(movie)).when(movieRepository).findById(1L);
        doReturn(Optional.of(renter)).when(renterRepository).findById(2L);

        underTest.rentAMovie(1L, 2L);

        assertThat(renter).isEqualTo(movie.getRenters().get(0));
        assertThat(2).isEqualTo(movie.getStock());

        verify(movieRepository).save(movie);
    }

    @Test
    void returnAMovie() {
        Movie movie = new Movie(1L, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
        MovieRenter renter = new MovieRenter(2L, "Marko", "Lazovic", "marko@gmail.com");

        movie.getRenters().add(renter);
        renter.getRentedMovies().add(movie);

        doReturn(Optional.of(movie)).when(movieRepository).findById(1L);
        doReturn(Optional.of(renter)).when(renterRepository).findById(2L);

        underTest.returnAMovie(1L, 2L);

        assertThat(4).isEqualTo(movie.getStock());
        assertThat(0).isEqualTo(movie.getRenters().size());

        verify(movieRepository).save(movie);
    }

    @Test
    void addMovieToDb() {
        Movie movie = new Movie(null, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);

        underTest.addMovieToDb(movie);

        verify(movieRepository).save(movie);
    }

    @Test
    void getAllMovies() {
        underTest.getAllMovies();

        verify(movieRepository).findAll();
    }

    @Test
    void getMovie() {
        Movie movie = new Movie(1L, "Titanik", 1995, Genre.DRAMA, "James Camoron", 3);
        doReturn(Optional.of(movie)).when(movieRepository).findById(1L);

        underTest.getMovie(1L);

        verify(movieRepository).findById(1L);
    }
}