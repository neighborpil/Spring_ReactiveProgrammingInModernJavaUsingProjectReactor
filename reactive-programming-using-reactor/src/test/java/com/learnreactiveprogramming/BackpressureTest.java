package com.learnreactiveprogramming;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureTest {

    // 백프레셔를 메뉴얼로 구현하는 방법(교육용, 안씀)
    @Test
    void testBackPressure() {

        Flux<Integer> numberRange = Flux.range(1, 100).log();

        // 실행하면 unbounded 라고 표시한되. 몇개가 돌아올지 알 수 없고 퍼블리셔가 주는대로 받는다
//        numberRange.subscribe(num -> {
//            log.info("Number is : {}", num);
//        });

        numberRange.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
//                super.hookOnNext(value);
                log.info("hookOnNext : {}", value);
                if (value == 2) {
                    cancel();
                }
            }

            @Override
            protected void hookOnCancel() {
//                super.hookOnCancel();
                log.info("Inside OnCancel");
            }

            @Override
            protected void hookOnComplete() {
//                super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
//                super.hookOnError(throwable);
            }
        });
    }

    @Test
    void testBackPressure_1() throws InterruptedException{

        Flux<Integer> numberRange = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
//                super.hookOnNext(value);
                log.info("hookOnNext : {}", value);
                if (value % 2 == 0 || value < 50) {
                    request(2);
                } else {
                    cancel();
                }
            }

            @Override
            protected void hookOnCancel() {
//                super.hookOnCancel();
                log.info("Inside OnCancel");
                latch.countDown();
            }

            @Override
            protected void hookOnComplete() {
//                super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
//                super.hookOnError(throwable);
            }
        });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressure_drop() throws InterruptedException{

        Flux<Integer> numberRange = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange
            .onBackpressureDrop(item -> {
                log.info("Dropped Items are : {}", item);
            })
            .subscribe(new BaseSubscriber<Integer>() {
                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    request(2);
                }

                @Override
                protected void hookOnNext(Integer value) {
//                super.hookOnNext(value);
                    log.info("hookOnNext : {}", value);
//                if (value % 2 == 0 || value < 50) {
//                    request(2);
//                } else {
//                    cancel();
//                }

                    if (value == 2) {
                        hookOnCancel();
                    }
                }

                @Override
                protected void hookOnCancel() {
//                super.hookOnCancel();
                    log.info("Inside OnCancel");
                    latch.countDown();
                }

                @Override
                protected void hookOnComplete() {
//                super.hookOnComplete();
                }

                @Override
                protected void hookOnError(Throwable throwable) {
//                super.hookOnError(throwable);
                }
            });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressure_buffer() throws InterruptedException{

        Flux<Integer> numberRange = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange
            .onBackpressureBuffer(10, i -> {
                log.info("Last Buffered element is : {}", i);
            })
            .subscribe(new BaseSubscriber<Integer>() {
                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    request(1);
                }

                @Override
                protected void hookOnNext(Integer value) {
//                super.hookOnNext(value);
                    log.info("hookOnNext : {}", value);
                    if (value < 50) {
                        request(1);
                    } else {
                        hookOnCancel();
                    }
                }

                @Override
                protected void hookOnCancel() {
//                super.hookOnCancel();
                    log.info("Inside OnCancel");
                    latch.countDown();
                }

                @Override
                protected void hookOnComplete() {
//                super.hookOnComplete();
                }

                @Override
                protected void hookOnError(Throwable throwable) {
//                super.hookOnError(throwable);
                }
            });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    void testBackPressure_error() throws InterruptedException{

        Flux<Integer> numberRange = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numberRange
            .onBackpressureError()
            .subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
//                super.hookOnNext(value);
                log.info("hookOnNext : {}", value);
                if (value < 50) {
                    request(1);
                } else {
                    hookOnCancel();
                }
            }

            @Override
            protected void hookOnCancel() {
//                super.hookOnCancel();
                log.info("Inside OnCancel");
                latch.countDown();
            }

            @Override
            protected void hookOnComplete() {
//                super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
//                super.hookOnError(throwable);
                log.error("Exception is : {}", throwable);
            }
        });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

}
