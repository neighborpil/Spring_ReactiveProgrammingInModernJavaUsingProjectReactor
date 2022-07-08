package com.learnreactiveprogramming;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {

    @Test
    void coldPublisherTest() {

        Flux<Integer> flux = Flux.range(1, 10);

        flux.subscribe(i -> System.out.println("Subscriber 1 : " + i));

        flux.subscribe(i -> System.out.println("Subscriber 2 : " + i));
    }

    @Test
    void hotPublisherTest() {

        Flux<Integer> flux = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<Integer> connectableFlux = flux.publish(); // make flux as a hot stream
        connectableFlux.connect();

        connectableFlux.subscribe(i -> System.out.println("Subscriber 1 : " + i));

        delay(4000);

        connectableFlux.subscribe(i -> System.out.println("Subscriber 2 : " + i));

        delay(10000);
    }

    @Test
    void hotPublisherTest_autoConnect() {

        Flux<Integer> flux = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1));

        var hotSource = flux.publish().autoConnect(2); // wait until at least 2 subscriber comes and emit

        hotSource.subscribe(i -> System.out.println("Subscriber 1 : " + i));

        delay(2000);

        hotSource.subscribe(i -> System.out.println("Subscriber 2 : " + i));
        System.out.println("Two Subscribers are connected");

        delay(2000);
        hotSource.subscribe(i -> System.out.println("Subscriber 3 : " + i));
        delay(10000);
    }

    @Test
    void hotPublisherTest_refCount() {

        Flux<Integer> flux = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1))
            .doOnCancel(() -> {
                System.out.println("Received Cancel Signal");
            });

        // refCount acts like a autoConnect but if count goes down, it stops emitting. and start from begining when condition matches
        var hotSource = flux.publish().refCount(2);

        Disposable disposable = hotSource.subscribe(i -> System.out.println("Subscriber 1 : " + i));

        delay(2000);

        Disposable disposable1 = hotSource.subscribe(i -> System.out.println("Subscriber 2 : " + i));
        System.out.println("Two Subscribers are connected");

        delay(2000);
        disposable.dispose();
        disposable1.dispose();
        hotSource.subscribe(i -> System.out.println("Subscriber 3 : " + i));
        delay(2000);
        hotSource.subscribe(i -> System.out.println("Subscriber 4 : " + i));
        delay(10000);
    }

}
