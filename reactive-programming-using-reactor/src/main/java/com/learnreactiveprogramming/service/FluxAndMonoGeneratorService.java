package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("alax", "ben", "chloe"))
            .log(); // db or remote service call

    }

    public Mono<String> nameMono() {

        return Mono.just("alex")
            .log();
    }

    public Flux<String> namesFlux_map(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase) // ALEX, BEN, CHLOE
//            .map(s -> s.toUpperCase())
            .filter(s -> s.length() > stringLength) // ALEX, CHLOE
            .map(s -> s.length() + "-" + s) // 4-ALEX, 5-CHLOE
            .log();
    }

    public Flux<String> namesFlux_immutability() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

        namesFlux.map(String::toUpperCase); // flux는 데이터를 코드 체인 이외에는 바꿀 수 없다

        return namesFlux;
    }

    public Mono<String> nameMono_filter(int stringLength) {

        return Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength);
    }

    public Flux<String> namesFlux_flatMap(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase) // ALEX, BEN, CHLOE
//            .map(s -> s.toUpperCase())
            .filter(s -> s.length() > stringLength)
            // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
            .flatMap(s -> splitString(s)) // A, L, E, X, C, H, L, O, E
            .log();
    }

    public Flux<String> splitString(String name) {

        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFlux_flatMap_async(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase) // ALEX, BEN, CHLOE
//            .map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString_withDelay(s)) // A, L, E, X, C, H, L, O, E // 비동기로 동작하여 순서를 생각하지 않는다, 빠르다
                .log();
    }

    public Flux<String> namesFlux_concatMap_async(int stringLength) {
        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase) // ALEX, BEN, CHLOE
//            .map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
                .concatMap(s -> splitString_withDelay(s)) // A, L, E, X, C, H, L, O, E // 비동기인데 순서를 지킨다, 모든 계산이 끝날때까지 기다리고 반환한다, 느리다
                .log();
    }

    public Flux<String> splitString_withDelay(String name) {

        var charArray = name.split("");
//        int delay = new Random().nextInt(1000);
        int delay = 1000;
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
            .subscribe(name ->{
                System.out.println("Name is : " + name);
            });

        fluxAndMonoGeneratorService.nameMono()
            .subscribe(name -> {
                System.out.println("Name is : " + name);
            });

    }

}
