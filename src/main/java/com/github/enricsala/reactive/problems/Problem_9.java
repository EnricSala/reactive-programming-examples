package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

final class Problem_9 {
    private Service service;

    /**
     * The caller cannot handle reactive types so we must return something else. For example this
     * could be an endpoint handler method in a Spring MVC 4 controller.
     */
    Iterable<Object> problem() {
        return service.findAll()
                .concatMap(service::lookup)
                .toIterable();
    }

    interface Service {
        Flux<String> findAll();
        Mono<Object> lookup(String foo);
    }
}
