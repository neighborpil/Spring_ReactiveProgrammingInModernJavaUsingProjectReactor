package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Revenue;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;
    private RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService, RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
    }

    public Flux<Movie> getAllMovies() {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
            .flatMap(movieInfo -> {
                Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(
                        movieInfo.getMovieInfoId())
                    .collectList();
                return reviewsMono.map(reviewsList -> new Movie(movieInfo, reviewsList));
            })
            .onErrorMap((ex) -> {
                log.error("Exception is : " + ex);
                throw new MovieException(ex.getMessage());
            })
            .log();
    }

    public Flux<Movie> getAllMovies_retry() {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
            .flatMap(movieInfo -> {
                Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(
                        movieInfo.getMovieInfoId())
                    .collectList();
                return reviewsMono.map(reviewsList -> new Movie(movieInfo, reviewsList));
            })
            .onErrorMap((ex) -> {
                log.error("Exception is : " + ex);
                throw new MovieException(ex.getMessage());
            })
//            .retry() // retry over and over again
            .retry(3)
            .log();
    }

    public Flux<Movie> getAllMovies_retryWhen() {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
            .flatMap(movieInfo -> {
                Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(
                        movieInfo.getMovieInfoId())
                    .collectList();
                return reviewsMono.map(reviewsList -> new Movie(movieInfo, reviewsList));
            })
            .onErrorMap((ex) -> {
                log.error("Exception is : " + ex);
                if (ex instanceof NetworkException) {
                    throw new MovieException(ex.getMessage());
                } else {
                    throw new ServiceException(ex.getMessage());
                }

            })
            .retryWhen(getRetryBackoffSpec())
            .log();
    }

    private RetryBackoffSpec getRetryBackoffSpec() {
        //        RetryBackoffSpec retryWhen = Retry.backoff(3, Duration.ofMillis(500))
//            .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));

        RetryBackoffSpec retryWhen = Retry.fixedDelay(3, Duration.ofMillis(500))
            .filter(ex -> ex instanceof MovieException) // MovieException일때만 재시도한다
            .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));
        return retryWhen;
    }

    public Mono<Movie> getMovieById(long movieId) {

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        Mono<List<Review>> reviewList = reviewService.retrieveReviewsFlux(movieId).collectList();

        return movieInfoMono.zipWith(reviewList, (movieInfo, reviews) -> new Movie(movieInfo, reviews));
    }

    public Flux<Movie> getAllMovies_repeat() {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
            .flatMap(movieInfo -> {
                Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(
                        movieInfo.getMovieInfoId())
                    .collectList();
                return reviewsMono.map(reviewsList -> new Movie(movieInfo, reviewsList));
            })
            .onErrorMap((ex) -> {
                log.error("Exception is : " + ex);
                if (ex instanceof NetworkException) {
                    throw new MovieException(ex.getMessage());
                } else {
                    throw new ServiceException(ex.getMessage());
                }

            })
            .retryWhen(getRetryBackoffSpec())
            .repeat() // 같은 작업을 2번 반복한다
            .log();
    }

    public Flux<Movie> getAllMovies_repeat_n(long n) {

        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
            .flatMap(movieInfo -> {
                Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(
                        movieInfo.getMovieInfoId())
                    .collectList();
                return reviewsMono.map(reviewsList -> new Movie(movieInfo, reviewsList));
            })
            .onErrorMap((ex) -> {
                log.error("Exception is : " + ex);
                if (ex instanceof NetworkException) {
                    throw new MovieException(ex.getMessage());
                } else {
                    throw new ServiceException(ex.getMessage());
                }

            })
            .retryWhen(getRetryBackoffSpec())
            .repeat(n) // 같은 작업을 n번 반복한다
            .log();
    }

    public Mono<Movie> getMovieById2(long movieId) {

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        return movieInfoMono
            .flatMap(movieInfo -> {
                Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                return reviewsMono.map(reviewsList -> new Movie(movieInfo, reviewsList));
            })
            .log();
    }

    public Mono<Movie> getMovieById_withRevenue(long movieId) {

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        Mono<List<Review>> reviewList = reviewService.retrieveReviewsFlux(movieId).collectList();

        Mono<Revenue> revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
            .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono.zipWith(reviewList, (movieInfo, reviews) -> new Movie(movieInfo, reviews))
            .zipWith(revenueMono, (movie, revenue) -> {
                movie.setRevenue(revenue);
                return movie;
            });
    }


}
