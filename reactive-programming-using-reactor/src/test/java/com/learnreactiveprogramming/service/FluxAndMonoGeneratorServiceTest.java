package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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
        Mono<String> namesMono = fluxAndMonoGeneratorService.nameMono_map_filter(stringLength);

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

    @Test
    void namesMono_flatMap() {

        int stringLength = 3;

        Mono<List<String>> value = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);

        StepVerifier.create(value)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany() {

        int stringLength = 3;

        Flux<String> value = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);

        StepVerifier.create(value)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {

        int stringLength = 3;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(3);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_1() {

        int stringLength = 6;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_withDefaultValue() {
        int stringLength = 6;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_withDefaultValue(stringLength);

        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_switchIfEmpty() {

        int stringLength = 6;

        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength);

        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();

    }

    @Test
    void nameMono_map_filter_defaultEmpty() {

        int stringLength = 4;
        Mono<String> namesMono = fluxAndMonoGeneratorService.nameMono_map_filter_defaultIfEmpty(stringLength);

        StepVerifier.create(namesMono)
                .expectNext("no_data")
                .verifyComplete();
    }
}
