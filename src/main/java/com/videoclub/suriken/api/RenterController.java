package com.videoclub.suriken.api;

import com.videoclub.suriken.model.MovieRenter;
import com.videoclub.suriken.service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("api/v1/renter")
@RestController
public class RenterController {

    private final RenterService renterService;

    @Autowired
    public RenterController(RenterService renterService) {
        this.renterService = renterService;
    }

    @PostMapping
    public void addRenter(@RequestBody MovieRenter movieRenter) {
        renterService.addMovieRenter(movieRenter);
    }

    @GetMapping
    public List<MovieRenter> getAllRenters() {
        return renterService.getAllRenters();
    }

    @GetMapping("/{id}")
    public MovieRenter getRenter(@PathVariable(name = "id") Long renterId) {
        return renterService.getRenter(renterId);
    }

}
