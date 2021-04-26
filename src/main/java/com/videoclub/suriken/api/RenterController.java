package com.videoclub.suriken.api;

import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.service.RenterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("api/v1/renter")
@RestController
public class RenterController {

    private final RenterService renterService;

    public RenterController(RenterService renterService) {
        this.renterService = renterService;
    }

    @PostMapping
    public ResponseEntity addRenter(@RequestBody MovieRenter movieRenter) {
        renterService.addMovieRenter(movieRenter);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MovieRenter>> getAllRenters() {
        return new ResponseEntity<>(renterService.getAllRenters(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieRenter> getRenter(@PathVariable(name = "id") Long renterId) {
        return new ResponseEntity<>(renterService.getRenter(renterId), HttpStatus.OK);
    }

}
