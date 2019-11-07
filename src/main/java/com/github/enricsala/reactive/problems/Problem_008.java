package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

final class Problem_008 {
    private Service service;

    Flux<String> problem() {
        return service.findAll()
                .flatMap(service::operation);
    }

    interface Service {
        Flux<String> findAll();
        Mono<String> operation(String foo);
    }
}
