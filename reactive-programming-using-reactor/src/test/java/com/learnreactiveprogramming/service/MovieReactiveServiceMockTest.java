package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    MovieReactiveService reactiveMovieService;


    @Test
    void getAllMovies() {

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenCallRealMethod();

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void getAllMovies_1() {

        var errorMessage = "Exception occurred";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenThrow(new RuntimeException(errorMessage));

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
//            .expectError(MovieException.class)
            .expectErrorMessage(errorMessage)
            .verify();
    }
}