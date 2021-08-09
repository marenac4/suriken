package com.videoclub.suriken.repository;

import com.videoclub.suriken.model.MovieRenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RenterRepository extends JpaRepository<MovieRenter, Long> {

    Optional<MovieRenter> findMovieRenterByFirstName(String firstName);

    Optional<MovieRenter> findMovieRenterByMail(String mail);

    @Query("SELECT r FROM MovieRenter r LEFT JOIN FETCH r.rentedMovies m WHERE r.id = ?1")
    Optional<MovieRenter> findRenterByIdAndRentedMovies(Long renterId);
}
