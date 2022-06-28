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

    @Test
    void nameMono_map_filter_switchIfEmpty() {
        int stringLength = 4;
        Mono<String> namesMono = fluxAndMonoGeneratorService.nameMono_map_filter_defaultIfEmpty(stringLength);

        StepVerifier.create(namesMono)
            .expectNext("no_data")
            .verifyComplete();
    }

    // concat

    @Test
    void explore_concat() {

        var concatFlux = fluxAndMonoGeneratorService.explore_concat();

        StepVerifier.create(concatFlux)
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();

    }

    @Test
    void explore_concatWith() {

        var concatFlux = fluxAndMonoGeneratorService.explore_concatWith();

        StepVerifier.create(concatFlux)
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();
    }

    @Test
    void explore_concatWith_mono() {

        var concatMono = fluxAndMonoGeneratorService.explore_concatWith_mono();

        StepVerifier.create(concatMono)
            .expectNext("A", "D")
            .verifyComplete();
    }

    @Test
    void explore_merge() {

        var concatFlux = fluxAndMonoGeneratorService.explore_merge();

        StepVerifier.create(concatFlux)
            .expectNext("A", "D", "B", "E", "C", "F")
            .verifyComplete();
    }

    @Test
    void explore_mergeWith() {

        var concatFlux = fluxAndMonoGeneratorService.explore_mergeWith();

        StepVerifier.create(concatFlux)
            .expectNext("A", "D", "B", "E", "C", "F")
            .verifyComplete();
    }


    @Test
    void explore_mergeWith_mono() {
        var concatFlux = fluxAndMonoGeneratorService.explore_mergeWith_mono();

        StepVerifier.create(concatFlux)
            .expectNext("A", "B")
            .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {

        var concatFlux = fluxAndMonoGeneratorService.explore_mergeSequential();

        StepVerifier.create(concatFlux)
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();
    }

    @Test
    void explore_zip() {

        var concatFlux = fluxAndMonoGeneratorService.explore_zip();

        StepVerifier.create(concatFlux)
            .expectNext("AD", "BE", "CF")
            .verifyComplete();
    }

    @Test
    void explore_zip_1() {

        var concatFlux = fluxAndMonoGeneratorService.explore_zip_1();

        StepVerifier.create(concatFlux)
        .expectNext("AD14", "BE25", "CF36")
            .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        var concatFlux = fluxAndMonoGeneratorService.explore_zipWith();

        StepVerifier.create(concatFlux)
            .expectNext("AD", "BE", "CF")
            .verifyComplete();
    }

    @Test
    void explore_zipWith_mono() {
        var zipFlux = fluxAndMonoGeneratorService.explore_zipWith_mono();

        StepVerifier.create(zipFlux)
            .expectNext("AB")
            .verifyComplete();
    }

    @Test
    void exception_flux() {

        Flux<String> value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value)
            .expectNext("A", "B", "C")
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void exception_flux_1() {

        Flux<String> value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value)
            .expectNext("A", "B", "C")
            .expectError()
            .verify();
    }

    @Test
    void exception_flux_2() {

        Flux<String> value = fluxAndMonoGeneratorService.exception_flux();

        StepVerifier.create(value)
            .expectNext("A", "B", "C")
            .expectErrorMessage("Exception Occured")
            .verify();
    }
}

