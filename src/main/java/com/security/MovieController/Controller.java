package com.security.MovieController;
import com.security.MovieDTO.MovieModel;
import com.security.MovieDTO.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")
public class Controller{

    @Autowired
    RestTemplate rs;

    @Autowired
    ExecutorService executor;

    @RequestMapping(value = "/movies")
    public List<MovieModel> getMovies() {

        String baseUrl = "https://jsonmock.hackerrank.com/api/moviesdata/search/";
        ResponseModel rm = rs.getForObject(baseUrl, ResponseModel.class);
        int totalPages = rm.getTotal();

        List<Future<List<MovieModel>>> movies = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            int page = i;
            movies.add(executor.submit(()->{
                String url = baseUrl+"&page="+page;
                return rs.getForObject(url, ResponseModel.class).getData();
            }));
        }
        List<MovieModel> result = new ArrayList<>();

        for (Future<List<MovieModel>>f : movies) {
            try {
                result.addAll(f.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @RequestMapping(value = "/movies" , params = "page")
    public ResponseModel getMovies(@RequestParam int page ,
                                      @RequestParam(required = false) String movieName,
                                      @RequestParam(required = false) Integer year) {

        String baseUrl = "https://jsonmock.hackerrank.com/api/moviesdata/search/?page="+page;

        if (movieName!=null) {
            baseUrl += "&Title=" + movieName;
        }
        ResponseModel rm = rs.getForObject(baseUrl, ResponseModel.class);
        if (year != null) {
            List<MovieModel> filtered = rm.getData().stream()
                    .filter(m -> m.getYear() == year)
                    .toList();

            rm.setData(filtered);
        }

        return rm;
    }
}
