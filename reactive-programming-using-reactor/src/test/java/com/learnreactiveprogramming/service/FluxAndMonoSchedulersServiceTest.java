package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoSchedulersServiceTest {

    FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void explore_publishOn() {

        var flux = fluxAndMonoSchedulersService.explore_publishOn();

        StepVerifier.create(flux)
            .expectNextCount(6)
            .verifyComplete();
    }

    @Test
    void explore_subscribeOn() {

        var flux = fluxAndMonoSchedulersService.explore_subscribeOn();

        StepVerifier.create(flux)
            .expectNextCount(6)
            .verifyComplete();
    }

    @Test
    void explore_parallel() {

        var flux = fluxAndMonoSchedulersService.explore_parallel();

        StepVerifier.create(flux)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap() {

        var flux = fluxAndMonoSchedulersService.explore_parallel();

        StepVerifier.create(flux)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void testExplore_parallel_usingFlatmap() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap();

        StepVerifier.create(flux)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap_1() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap_1();

        StepVerifier.create(flux)
            .expectNextCount(6)
            .verifyComplete();

    }

    @Test
    void explore_parallel_usingFlatmapSequential() {

        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmapSequential();

        StepVerifier.create(flux)
//            .expectNextCount(3)
            .expectNext("ALEX", "BEN", "CHLOE")
            .verifyComplete();

    }
}