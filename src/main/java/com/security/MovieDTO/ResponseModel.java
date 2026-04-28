package com.security.MovieDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonPropertyOrder({ "page", "per_page", "total", "total_pages", "data" })
@Data
public class ResponseModel {
    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<MovieModel> data;
}
