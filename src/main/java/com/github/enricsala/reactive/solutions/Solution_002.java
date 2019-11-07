package com.github.enricsala.reactive.solutions;

import reactor.core.publisher.Mono;

/**
 * The problem is that {@link Mono#doOnNext} does not subscribe to the {@link Mono} returned by
 * {@link Service#update}, so the update of the element emitted by {@link Service#create} would not
 * be subscribed.
 */
final class Solution_002 {
    private Service service;

    /** Use {@link Mono#flatMap} so that the inner {@link Mono} is subscribed. */
    Mono<Void> solution() {
        return service.create("foo")
                .flatMap(service::update);
    }

    interface Service {
        Mono<String> create(String foo);
        Mono<Void> update(String foo);
    }
}
