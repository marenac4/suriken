package com.videoclub.suriken.service.impl;

import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.RenterRepository;
import com.videoclub.suriken.service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenterServiceImpl implements RenterService {

    private final RenterRepository renterRepository;

    public RenterServiceImpl(RenterRepository renterRepository) {
        this.renterRepository = renterRepository;
    }

    @Override
    public void addMovieRenter(MovieRenter movieRenter) {
        renterRepository.save(movieRenter);
    }

    @Override
    public List<MovieRenter> getAllRenters() {
        return renterRepository.findAll();
    }

    @Override
    public MovieRenter getRenter(Long renterId) {
        MovieRenter movieRenter = renterRepository.findById(renterId).orElseThrow();

        System.out.println("-------------------- When get movie is called ---------------------");

        movieRenter.getRentedMovies()
                .forEach(movie -> System.out.println("DEBUG: Renter is renting : " + movie.getName()));

        return movieRenter;
    }
}
