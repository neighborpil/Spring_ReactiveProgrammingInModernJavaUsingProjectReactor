package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MovieReactiveServiceTest {

    private MovieInfoService movieInfoService = new MovieInfoService();
    private ReviewService reviewService = new ReviewService();
    private RevenueService revenueService = new RevenueService();

    private MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService,
        reviewService, revenueService);
    @Test
    void getAllMovies() {

        Flux<Movie> moviesFlux = movieReactiveService.getAllMovies();

        StepVerifier.create(moviesFlux)
            .assertNext(movie -> {
                assertEquals("Batman Begins", movie.getMovieInfo().getName());
                assertEquals(2, movie.getReviewList().size());
                // name of the movie
                //reviesList
            })
            .assertNext(movie -> {
                assertEquals("The Dark Knight", movie.getMovieInfo().getName());
                assertEquals(2, movie.getReviewList().size());
                // name of the movie
                //reviesList
            })
            .assertNext(movie -> {
                assertEquals("Dark Knight Rises", movie.getMovieInfo().getName());
                assertEquals(2, movie.getReviewList().size());
                // name of the movie
                //reviesList
            })
            .verifyComplete();
    }

    @Test
    void getMovieById() {

        long movieId = 100L;

        Mono<Movie> movieMono = movieReactiveService.getMovieById(movieId).log();

        StepVerifier.create(movieMono)
            .assertNext(movie -> {
                assertEquals("Batman Begins", movie.getMovieInfo().getName());
                assertEquals(2, movie.getReviewList().size());
            })
            .verifyComplete();
    }

    @Test
    void getMovieById2() {

        long movieId = 100L;

        Mono<Movie> movieMono = movieReactiveService.getMovieById2(movieId);

        StepVerifier.create(movieMono)
            .assertNext(movie -> {
                assertEquals("Batman Begins", movie.getMovieInfo().getName());
                assertEquals(2, movie.getReviewList().size());
            })
            .verifyComplete();
    }

    @Test
    void getMovieById_withRevenue() {

        long movieId = 100L;

        Mono<Movie> movieMono = movieReactiveService.getMovieById_withRevenue(movieId).log();

        StepVerifier.create(movieMono)
            .assertNext(movie -> {
                assertEquals("Batman Begins", movie.getMovieInfo().getName());
                assertEquals(2, movie.getReviewList().size());
                assertNotNull(movie.getRevenue());
            })
            .verifyComplete();
    }

}