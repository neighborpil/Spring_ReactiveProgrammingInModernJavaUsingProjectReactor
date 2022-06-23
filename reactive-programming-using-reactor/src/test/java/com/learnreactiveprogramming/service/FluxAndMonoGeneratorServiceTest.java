package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
            .expectNext("alax", "ben", "chloe")
            .verifyComplete();
    }

    @Test
    void namesFlux_oneNextAndTwoCount() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
            .expectNext("alax")
            .expectNextCount(2)
            .verifyComplete();
    }

    @Test
    void namesFlux_checkCount() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(namesFlux)
//            .expectNext("alax", "ben", "chloe")
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void namesMono() {

        Mono<String> namesMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(namesMono)
            .expectNext("alex")
            .verifyComplete();
    }

    @Test
    void namesFlux_map() {

        int stringLength = 3;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        StepVerifier.create(namesFlux)
//            .expectNext("ALEX", "BEN", "CHLOE")
//            .expectNext("ALEX", "CHLOE")
            .expectNext("4-ALEX", "5-CHLOE")
            .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

        StepVerifier.create(namesFlux)
            .expectNext("alex", "ben", "chloe")
            .verifyComplete();
    }

    @Test
    void nameMono_filter() {

        int stringLength = 3;
        Mono<String> namesMono = fluxAndMonoGeneratorService.nameMono_filter(stringLength);

        StepVerifier.create(namesMono)
            .expectNext("ALEX")
            .verifyComplete();
    }

    @Test
    void namesFlux_flatMap() {

        int stringLength = 3;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatMap(stringLength);

        StepVerifier.create(namesFlux)
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }

    @Test
    void namesFlux_flatMap_async() {

        int stringLength = 3;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatMap_async(stringLength);

        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap_async() {

        int stringLength = 3;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap_async(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap_async_count() {

        int stringLength = 3;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap_async(stringLength);

        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }
}
