package com.security.moviesearchapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("movie.api")
@Component
public class UrlConfig {

    private String url;

}
