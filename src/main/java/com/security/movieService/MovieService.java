package com.security.movieService;

import com.security.movieDTO.MovieModel;
import com.security.movieDTO.ResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;

@Service
public class MovieService {

    private final RestTemplate rs;

    private final ExecutorService executor;

    public MovieService(RestTemplate rs, ExecutorService executor) {
        this.rs = rs;
        this.executor = executor;
    }

    public List<MovieModel> getMovies(){

        String baseUrl = "https://jsonmock.hackerrank.com/api/moviesdata/search/";
        ResponseModel rm = rs.getForObject(baseUrl, ResponseModel.class);
        int totalPages = rm.getTotal_pages();

        List<CompletableFuture<List<MovieModel>>> movies = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            int page = i;
            movies.add(CompletableFuture.supplyAsync(() -> {
                String url = baseUrl+"&page="+page;
                return rs.getForObject(url, ResponseModel.class).getData();
            }, executor));
        }
        List<MovieModel> result = new ArrayList<>();

        for (CompletableFuture<List<MovieModel>> f : movies) {
            result.addAll(f.join());
        }

        return result;
    }

    public ResponseModel getMovies(int page, String movieName, Integer year) {
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
