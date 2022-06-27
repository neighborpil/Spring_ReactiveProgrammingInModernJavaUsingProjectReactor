package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
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
            .log();

    }

}
