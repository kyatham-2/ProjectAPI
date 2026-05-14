package com.security.moviesearchapi.service;

import com.security.moviesearchapi.dto.MovieModel;
import com.security.moviesearchapi.dto.ResponseModel;
import com.security.moviesearchapi.config.UrlConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class MovieService {

    private final RestTemplate rs;
    private final ExecutorService executor;
    private final String urlConfig;

    public MovieService(RestTemplate rs, ExecutorService executor, UrlConfig urlConfig) {
        this.rs = rs;
        this.executor = executor;
        this.urlConfig = urlConfig.getUrl();
    }

    public List<MovieModel> getMovies(){
        String baseUrl = urlConfig;
        ResponseModel rm = rs.getForObject(baseUrl, ResponseModel.class);
        int totalPages = rm.getTotal_pages();

        List<CompletableFuture<List<MovieModel>>> movies = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            int page = i;
            movies.add(CompletableFuture.supplyAsync(() -> {
                String url = baseUrl+"&page="+page;
                return rs.getForObject(url, ResponseModel.class).getData();
            }, executor)
                    .completeOnTimeout(Collections.emptyList(), 500, TimeUnit.MILLISECONDS));
        }
        List<MovieModel> result = new ArrayList<>();

        for (CompletableFuture<List<MovieModel>> f : movies) {
            result.addAll(f.join());
        }

        return result;
    }

    public ResponseModel getMovies(int page, String movieName, Integer year) {
        String url = urlConfig+"?page="+page;
        if (movieName!=null) {
            url += "&Title=" + movieName;
        }
        ResponseModel rm = rs.getForObject(url, ResponseModel.class);
        if (year != null) {
            List<MovieModel> filtered = rm.getData().stream()
                    .filter(m -> m.getYear() == year)
                    .toList();

            rm.setData(filtered);
        }

        return rm;
    }
}
