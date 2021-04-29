package com.videoclub.suriken.advice;

import com.videoclub.suriken.dto.RestMessage;
import com.videoclub.suriken.exceptions.NoMovieInStockException;
import com.videoclub.suriken.exceptions.RestException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    private static final String MOVIE_NOT_FOUND = "Exception.movieNotFound";

    private final MessageSource messageSource;

    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({RestException.class})
    public ResponseEntity<Object> handleMovieNotFoundException(final RestException ex, final WebRequest request) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), request.getLocale());
        RestMessage message = new RestMessage(RestMessage.MessageType.RESOURCE_NOT_FOUND, errorMessage);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoMovieInStockException.class})
    protected ResponseEntity<Object> handleNoMovieInStockException(final NoMovieInStockException ex, final WebRequest request) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), request.getLocale());
        RestMessage message = new RestMessage(RestMessage.MessageType.NO_MOVIE_IN_STOCK, errorMessage);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


}
