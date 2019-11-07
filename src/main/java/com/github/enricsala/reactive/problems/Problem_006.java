package com.github.enricsala.reactive.problems;

import java.util.concurrent.atomic.AtomicInteger;
import reactor.core.publisher.Flux;

final class Problem_006 {
    private Service service;

    Flux<Integer> problem() {
        AtomicInteger count = new AtomicInteger();
        return service.findAll()
                .map(Integer::valueOf)
                .doOnNext(count::addAndGet)
                .doOnComplete(() -> System.out.println("Total: " + count.get()));
    }

    interface Service {
        Flux<String> findAll();
    }
}
