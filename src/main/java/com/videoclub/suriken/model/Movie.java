package com.videoclub.suriken.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private int year;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String director;

    private int stock;

    @ManyToMany()
    private List<MovieRenter> renters = new ArrayList<>();

    public Movie(Long id,String name, int year, Genre genre, String director, int stock, List<MovieRenter> renters) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.stock = stock;
        this.renters = renters;
    }

    public Movie() {
    }

    public void addMovieRenter(MovieRenter movieRenter) {
        renters.add(movieRenter);
        movieRenter.addRentedMovie(this);
    }

    public void removeMovieRenter(MovieRenter movieRenter) {
        renters.remove(movieRenter);
        movieRenter.removeRentedMovie(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public int getStock() {
        return stock;
    }

    public List<MovieRenter> getRenters() {
        return renters;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setRenters(List<MovieRenter> renters) {
        this.renters = renters;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", genre=" + genre +
                ", director='" + director + '\'' +
                ", stock=" + stock +
                '}';
    }

    public void decrementStockValueByOne() {
        if (stock > 0) stock--;
    }

    public void incrementStockByOne() {
        stock++;
    }
}
