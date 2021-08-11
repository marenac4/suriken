package com.videoclub.suriken.security.repository;

import com.videoclub.suriken.security.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT au FROM AppUser au LEFT JOIN FETCH au.roles r LEFT JOIN FETCH r.privileges p WHERE au.userName = ?1")
    Optional<AppUser> findAppUserByUserName(String userName);
}
