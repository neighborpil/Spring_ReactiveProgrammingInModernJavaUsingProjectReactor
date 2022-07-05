package com.learnreactiveprogramming;

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
}
