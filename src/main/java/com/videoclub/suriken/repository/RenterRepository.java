package com.videoclub.suriken.repository;

import com.videoclub.suriken.model.MovieRenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenterRepository extends JpaRepository<MovieRenter, Long> {
}
