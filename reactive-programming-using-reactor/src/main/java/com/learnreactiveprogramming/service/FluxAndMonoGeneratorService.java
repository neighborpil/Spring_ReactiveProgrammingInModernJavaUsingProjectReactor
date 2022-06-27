package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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
    public Mono<String> nameMono_map_filter(int stringLength) {

        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .log();
    }

    public Mono<String> nameMono_map_filter_defaultIfEmpty(int stringLength) {

        return Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength)
            .defaultIfEmpty("no_data")
            .log();
    }

    public Mono<String> nameMono_map_filter_switchIfEmpty(int stringLength) {

        Mono<String> defaultValue = Mono.just("no_data");

        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .switchIfEmpty(defaultValue)
                .log();
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

    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        // filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s)) // A, L, E, X, C, H, L, O, E
                .log();
    }

    public Flux<String> namesFlux_transform_withDefaultValue(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        // Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        Flux<String> defaultFlux = Flux.just("default").transform(filterMap);

        // Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .flatMap(s -> splitString(s))
                .log();
    }

    // concat

    public Flux<String> explore_concat() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> explore_concatWith() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_mono() {

        Mono<String> aFlux = Mono.just("A");
        Mono<String> dFlux = Mono.just("D");

        return aFlux.concatWith(dFlux).log(); // "A", "B"
    }

    // merge 행력식으로 하나씩 끼워넣는다, 두개의 flux에서 먼저 오는 것을 처리한다
    public Flux<String> explore_merge() {

        Flux<String> abcFlux = Flux.just("A", "B", "C")
            .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
            .delayElements(Duration.ofMillis(120));

        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> explore_mergeWith() {

        Flux<String> abcFlux = Flux.just("A", "B", "C")
            .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
            .delayElements(Duration.ofMillis(120));

        return abcFlux.mergeWith(defFlux).log();
    }

    // merge와 같으나 instance Function이다
    public Flux<String> explore_mergeWith_mono() {

        Mono<String> aMono= Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.mergeWith(bMono).log();
    }

    // mergeSequential, 동시에 데이터를 가져오나 결과는 순차적으로 배열한다

    public Flux<String> explore_mergeSequential() {

        Flux<String> abcFlux = Flux.just("A", "B", "C")
            .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
            .delayElements(Duration.ofMillis(120));

        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    // zip, zipWith - 변수 3개(2개 플럭스, 람다 펑션), 행렬연산한다, 2~8개 플럭스를 합칠 수 있다
    // 하나의 element를 만들기 위하여 모든 퍼블리셔들이 참가
    // 하나의 퍼블리셔가 OnComplete이벤트를 보낼때까지 계속한다
    public Flux<String> explore_zip() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log();
        // AD, BE, CF
    }

    // 플럭스가 2개 이상이면 튜플을 반환하고 처리한다
    public Flux<String> explore_zip_1() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        Flux<String> _123Flux = Flux.just("1", "2", "3");

        Flux<String> _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
            .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4()).log();
        // AD14, BE25, CF36
    }

    public Flux<String> explore_zipWith() {

        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();
        // AD, BE, CF
    }

    public Mono<String> explore_zipWith_mono() {

        Mono<String> aMono= Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.zipWith(bMono)
            .map(t2 -> t2.getT1() + t2.getT2())
            .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        List<String> charList = List.of(charArray);// ALEX -> A, L, E, X
        return Mono.just(charList);
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
