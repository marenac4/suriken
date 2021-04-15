package com.videoclub.suriken.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long movId;

    private String movName;

    private int movYear;

    @Enumerated(EnumType.STRING)
    private Genre movGenre;

    private String movDirector;

    private int stock;

    @ManyToMany()
    private List<MovieRenter> movieRenters = new ArrayList<>();

    public Movie(Long movId,
                 String movName,
                 int movYear,
                 Genre movGenre,
                 String movDirector,
                 int stock,
                 List<MovieRenter> movieRenters) {
        this.movId = movId;
        this.movName = movName;
        this.movYear = movYear;
        this.movGenre = movGenre;
        this.movDirector = movDirector;
        this.stock = stock;
        this.movieRenters = movieRenters;
    }

    public Movie() {
    }

    public void addMovieRenter(MovieRenter movieRenter) {
        movieRenters.add(movieRenter);
        movieRenter.addRentedMovie(this);
    }

    public void removeMovieRenter(MovieRenter movieRenter) {
//        for (int i = 0; i < movieRenters.size(); i++)
//            if (movieRenters.get(i).getRenterId().equals(movieRenter.getRenterId())) {
//                movieRenters.remove(i);
//                break;
//            }

        movieRenters.remove(movieRenter);
        movieRenter.removeRentedMovie(this);
    }

    public Long getMovId() {
        return movId;
    }

    public String getMovName() {
        return movName;
    }

    public int getMovYear() {
        return movYear;
    }

    public Genre getMovGenre() {
        return movGenre;
    }

    public String getMovDirector() {
        return movDirector;
    }

    public int getStock() {
        return stock;
    }

    public List<MovieRenter> getMovieRenters() {
        return movieRenters;
    }

    public void setMovId(Long movId) {
        this.movId = movId;
    }

    public void setMovName(String movName) {
        this.movName = movName;
    }

    public void setMovYear(int movYear) {
        this.movYear = movYear;
    }

    public void setMovGenre(Genre movGenre) {
        this.movGenre = movGenre;
    }

    public void setMovDirector(String movDirector) {
        this.movDirector = movDirector;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMovieRenters(List<MovieRenter> movieRenters) {
        this.movieRenters = movieRenters;
    }

    public boolean decrementStockValueByOne() {
        if (stock > 0) {
            stock--;
            return true;
        } else {
            return false;
        }
    }

    public void incrementStockByOne() {
        stock++;
    }

    public enum Genre {
        Thriller ("Thriller"),
        Drama ("Drama"),
        Comedy ("Comedy");

        private String name;

        public String getName() {
            return name;
        }

        Genre(String name) {
            this.name = name;
        }
    }
}
