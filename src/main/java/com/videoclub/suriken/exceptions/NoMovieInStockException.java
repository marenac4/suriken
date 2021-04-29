package com.videoclub.suriken.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when there in no selected Movie in the stock
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoMovieInStockException extends RestException{

    public NoMovieInStockException(String message, Object[] args) {
        super(message, args);
    }

}
