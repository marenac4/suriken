package com.videoclub.suriken.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MovieRenter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String firstName;

    private String lastName;

    private String mail;

    @ManyToMany(mappedBy = "movieRenters")
    @JsonIgnore
    private List<Movie> rentedMovies = new ArrayList<>();

    public MovieRenter(Long id, String firstName, String lastName, String mail, List<Movie> rentedMovies) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.rentedMovies = rentedMovies;
    }

    public MovieRenter(){

    }

    public void addRentedMovie(Movie movie) {
        rentedMovies.add(movie);
    }

    public void removeRentedMovie(Movie movie) {
        System.out.println("Renter : " + this.getFirstName() + "removeRentedMovie method inside model class" + rentedMovies.size());
        rentedMovies.remove(movie);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public List<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRentedMovies(List<Movie> rentedMovies) {
        this.rentedMovies = rentedMovies;
    }
}
