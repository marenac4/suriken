package com.videoclub.suriken.service;

import com.videoclub.suriken.model.MovieRenter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RenterService {

    void addMovieRenter(MovieRenter movieRenter);

    List<MovieRenter> getAllRenters();

    MovieRenter getRenter(Long renterId);
}
