package com.security.movieController;
import com.security.movieDTO.MovieModel;
import com.security.movieDTO.ResponseModel;
import com.security.movieService.MovieService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping(value = "/movies")
    public List<MovieModel> getMovies() {
        return movieService.getMovies();
    }

    @RequestMapping(value = "/movies" , params = "page")
    public ResponseModel getMovies(@RequestParam int page ,
                                      @RequestParam(required = false) String movieName,
                                      @RequestParam(required = false) Integer year) {

        return movieService.getMovies(page, movieName, year);

    }
}
