package com.videoclub.suriken.service.impl;

import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.RenterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RenterServiceImplTest {

    @Mock
    private RenterRepository renterRepository;

    @InjectMocks
    private RenterServiceImpl underTest;

    @Test
    void addMovieRenter() {
        MovieRenter renter = new MovieRenter(null, "Marko", "Lazovic", "marko@gmail.com");

        underTest.addMovieRenter(renter);

        verify(renterRepository).save(renter);
    }

    @Test
    void getAllRenters() {
        underTest.getAllRenters();

        verify(renterRepository).findAll();
    }

    @Test
    void getRenter() {
        MovieRenter renter = new MovieRenter(1L, "Marko", "Lazovic", "marko@gmail.com");

        doReturn(Optional.of(renter)).when(renterRepository).findById(1L);

        underTest.getRenter(1L);

        verify(renterRepository).findById(1L);
    }
}
