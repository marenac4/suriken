package com.videoclub.suriken.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class MovieRenter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long renterId;

    private String renterName;

    private String renterLastName;

    private String renterMail;

    @ManyToMany(mappedBy = "movieRenters")
    @JsonIgnore
    private List<Movie> rentedMovies = new ArrayList<>();

    public MovieRenter(Long renterId, String renterName, String renterLastName, String renterMail, List<Movie> rentedMovies) {
        this.renterId = renterId;
        this.renterName = renterName;
        this.renterLastName = renterLastName;
        this.renterMail = renterMail;
        this.rentedMovies = rentedMovies;
    }

    public MovieRenter(){

    }

    public void addRentedMovie(Movie movie) {
        rentedMovies.add(movie);
    }

    public void removeRentedMovie(Movie movie) {
        System.out.println("Renter : " + this.getRenterName() + "removeRentedMovie method inside model class" + rentedMovies.size());
        rentedMovies.remove(movie);
    }

    public Long getRenterId() {
        return renterId;
    }

    public String getRenterName() {
        return renterName;
    }

    public String getRenterLastName() {
        return renterLastName;
    }

    public String getRenterMail() {
        return renterMail;
    }

    public List<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public void setRenterId(Long userId) {
        this.renterId = userId;
    }

    public void setRenterMail(String userMail) {
        this.renterMail = userMail;
    }

    public void setRenterName(String userName) {
        this.renterName = userName;
    }

    public void setRenterLastName(String userLastName) {
        this.renterLastName = userLastName;
    }

    public void setRentedMovies(List<Movie> rentedMovies) {
        this.rentedMovies = rentedMovies;
    }
}
