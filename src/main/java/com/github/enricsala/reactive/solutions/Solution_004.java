package com.github.enricsala.reactive.solutions;

import java.io.IOException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * The problem is that {@link Mono#subscribe} may or may not run asynchronously depending on whether
 * a {@link Scheduler} has been configured. Since we don't know and should not care about how {@link
 * Service#update} is implemented, we should not assume it will be blocking. If a {@link Scheduler}
 * has been applied, the subscription will be scheduled on another thread and {@link Mono#subscribe}
 * will return immediately, so any exception thrown during its execution will not be caught by the
 * try/catch.
 */
final class Solution_004 {
    private Service service;

    /**
     * Using {@link Mono#block} solves the problem because the caller thread will have to wait to
     * completion and any exception will be rethrown (use sparingly).
     *
     * <p>However, be aware the {@link Mono#block} cannot throw a checked exception, so for example
     * a thrown {@link IOException} will be wrapped as {@link RuntimeException}.
     */
    void solution_1() {
        try {
            service.update("foo").block();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Use the built-in support for error handling in reactive programming, and return the {@link
     * Mono} to let the caller handle it.
     */
    Mono<Void> solution_2() {
        return service.update("foo")
                .doOnError(Throwable::printStackTrace);
    }

    interface Service {
        Mono<Void> update(String foo);
    }
}
