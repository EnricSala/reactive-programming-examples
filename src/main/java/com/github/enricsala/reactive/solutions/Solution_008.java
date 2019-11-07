package com.github.enricsala.reactive.solutions;

import java.util.concurrent.Flow.Publisher;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.concurrent.Queues;

/**
 * The problem is that {@link Flux#flatMap(Function)} subscribes to inner {@link Publisher}s
 * eagerly, meaning that there could be a large amount of concurrent subscriptions if the
 * implementation of {@link Service#operation} uses a {@link Scheduler}.
 *
 * <p>In practice, the concurrency would be limited by the default concurrency of the {@link
 * Flux#flatMap(Function)} operator, which is defined in {@link Queues#SMALL_BUFFER_SIZE}.
 *
 * <p>Therefore, it is never advisable to use {@link Flux#flatMap(Function)} without specifying the
 * concurrency, because it can lead to unintended parallel execution.
 *
 * <p>Even better, prefer using {@link Flux#concatMap(Function)} by default to ensure sequential
 * subscription unless parallelism is required.
 */
final class Solution_008 {
    private Service service;

    /** Limit the maximum concurrency using the {@link Flux#flatMap(Function, int)} overload. */
    Flux<String> solution_1() {
        return service.findAll()
                .flatMap(service::operation, 4);
    }

    /** Concurrency is not necessary? Then let's not allow it. */
    Flux<String> solution_2() {
        return service.findAll()
                .concatMap(service::operation);
    }

    interface Service {
        Flux<String> findAll();
        Mono<String> operation(String foo);
    }
}
