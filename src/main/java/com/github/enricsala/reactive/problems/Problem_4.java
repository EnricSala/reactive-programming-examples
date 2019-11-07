package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Mono;

final class Problem_4 {
    private Service service;

    void problem() {
        try {
            service.update("foo").subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface Service {
        Mono<Void> update(String foo);
    }
}
