package com.videoclub.suriken.repository;

import com.videoclub.suriken.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findMovieByName(String name);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.renters r WHERE m.id = ?1")
    Optional<Movie> findMovieByIdAndTheirRenters(Long movieId);

}
