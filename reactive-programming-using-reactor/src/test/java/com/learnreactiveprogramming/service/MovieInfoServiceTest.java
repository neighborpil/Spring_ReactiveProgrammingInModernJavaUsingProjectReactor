package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;

import com.learnreactiveprogramming.domain.MovieInfo;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:8080/movies")
        .build();

    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void retrieveAllMovieInfo_RestClient() {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();

        StepVerifier.create(movieInfoFlux)
            .expectNextCount(7)
            .verifyComplete();

    }

    @Test
    void retrieveAllMovieInfoById_RestClient() {

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(1);

        StepVerifier.create(movieInfoMono)
            .expectNextCount(1)
            .verifyComplete();
    }
}