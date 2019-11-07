package com.github.enricsala.reactive.solutions;

import reactor.core.publisher.Mono;

/**
 * The problem is that the {@link Service#update} would not be executed because the returned {@link
 * Mono} is ignored and therefore not subscribed.
 */
final class Solution_001 {
    private Service service;

    /** Apply a terminal operation to execute it (use sparingly). */
    void solution_1() {
        service.update("foo").block();
    }

    /** Return the Mono to let the caller handle it. */
    Mono<Void> solution_2() {
        return service.update("foo");
    }

    interface Service {
        Mono<Void> update(String foo);
    }
}
