package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.learnreactiveprogramming.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ReviewServiceTest {

    WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:8080/movies")
        .build();

    ReviewService reviewService = new ReviewService(webClient);

    @Test
    void retrieveReviewsFlux_RestClient() {

        var movieInfoId = 1L;

        Flux<Review> reviewFlux = reviewService.retrieveReviewsFlux_RestClient(movieInfoId);

        StepVerifier.create(reviewFlux)
            .assertNext(r -> assertEquals(r.getReviewId(), 1))
            .verifyComplete();
    }
}