package com.videoclub.suriken.service.impl;

import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.repository.RenterRepository;
import com.videoclub.suriken.service.RenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenterServiceImpl implements RenterService {

    private final RenterRepository renterRepository;

    Logger logger = LoggerFactory.getLogger(RenterServiceImpl.class);

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

        logger.debug("-------------------- When get movie is called ---------------------");

        movieRenter.getRentedMovies()
                .forEach(movie -> logger.debug("Renter is renting : {}", movie));

        return movieRenter;
    }
}
