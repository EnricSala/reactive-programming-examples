package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Mono;

final class Problem_1 {
    private Service service;

    void problem() {
        service.update("foo");
    }

    interface Service {
        Mono<Void> update(String foo);
    }
}
