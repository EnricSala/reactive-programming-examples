package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

final class Problem_10 {
    private Service service;
    private Repository repository;

    void startLongRunningTask() {
        service.observe()
                .switchMap(repository::save)
                .subscribeOn(Schedulers.elastic())
                .doOnSubscribe(unused -> System.out.println("Starting..."))
                .subscribe();
    }

    interface Service {
        /** Returns an infinite stream that randomly emits items. */
        Flux<Object> observe();
    }

    interface Repository {
        Mono<Void> save(Object content);
        Mono<Object> getLatest();
    }
}
