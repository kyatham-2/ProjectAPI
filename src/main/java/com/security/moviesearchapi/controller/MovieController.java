package com.security.moviesearchapi.controller;
import com.security.moviesearchapi.dto.MovieModel;
import com.security.moviesearchapi.dto.ResponseModel;
import com.security.moviesearchapi.service.MovieService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/movies")
    public List<MovieModel> getMovies() {
        return movieService.getMovies();
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/movies" , params = "page")
    public ResponseModel getMovies(@RequestParam int page ,
                                      @RequestParam(required = false) String movieName,
                                      @RequestParam(required = false) Integer year) {

        return movieService.getMovies(page, movieName, year);

    }
}
