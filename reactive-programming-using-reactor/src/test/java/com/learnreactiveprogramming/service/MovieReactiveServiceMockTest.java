package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ReactorException;
import com.learnreactiveprogramming.exception.ServiceException;
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

    @Test
    void getAllMovies_retry() {

        var errorMessage = "Exception occurred";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenThrow(new RuntimeException(errorMessage));

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies_retry();

        StepVerifier.create(moviesFlux)
//            .expectError(MovieException.class)
            .expectErrorMessage(errorMessage)
            .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    void getAllMovies_retryWhen_ServiceException() {

        var errorMessage = "Exception occurred";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenThrow(new ServiceException(errorMessage));

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

        StepVerifier.create(moviesFlux)
//            .expectError(MovieException.class)
            .expectErrorMessage(errorMessage)
            .verify();

        verify(reviewService, times(1)).retrieveReviewsFlux(isA(Long.class));
    }
    @Test
    void getAllMovies_retryWhen_NetworkException() {

        var errorMessage = "Exception occurred";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenThrow(new NetworkException(errorMessage));

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies_retryWhen();

        StepVerifier.create(moviesFlux)
//            .expectError(MovieException.class)
            .expectErrorMessage(errorMessage)
            .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));
    }


    @Test
    void getAllMovies_repeat() {
        var errorMessage = "Exception occurred";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenCallRealMethod();

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies_repeat();

        StepVerifier.create(moviesFlux)
//            .expectError(MovieException.class)
            .expectNextCount(6)
            .thenCancel()
            .verify();

        verify(reviewService, times(6))
            .retrieveReviewsFlux(isA(Long.class));
    }
    @Test
    void getAllMovies_repeat_n() {
        var errorMessage = "Exception occurred";

        Mockito.when(movieInfoService.retrieveMoviesFlux())
            .thenCallRealMethod();

        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
            .thenCallRealMethod();

        var noOfTimes = 2L;

        Flux<Movie> moviesFlux = reactiveMovieService.getAllMovies_repeat_n(noOfTimes);

        StepVerifier.create(moviesFlux)
//            .expectError(MovieException.class)
            .expectNextCount(9)
            .verifyComplete();

        verify(reviewService, times(9))
            .retrieveReviewsFlux(isA(Long.class));
    }
}