package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MovieReactiveServiceTest {

    private MovieInfoService movieInfoService = new MovieInfoService();
    private ReviewService reviewService = new ReviewService();

    private MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService,
        reviewService);
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
}