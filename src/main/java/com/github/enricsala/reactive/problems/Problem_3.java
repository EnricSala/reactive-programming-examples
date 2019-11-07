package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Mono;

final class Problem_3 {
    private Service service;

    Mono<Void> problem() {
        return service.create("foo")
                .doOnNext(foo -> service.update(foo).block())
                .then();
    }

    interface Service {
        Mono<String> create(String foo);
        Mono<Void> update(String foo);
    }
}
