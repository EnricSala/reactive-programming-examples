package com.github.enricsala.reactive.solutions;

import java.util.concurrent.atomic.AtomicInteger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The problem is that the returned {@link Flux} is not safe to resubscribe. If the caller
 * subscribes multiple times then we will keep incrementing the same counter. The returned {@link
 * Flux} holds a reference to the original counter because it is created eagerly, so all
 * subscriptions will use the same instance.
 *
 * <p>This can happen for example when using {@link Flux#repeat} or if the caller adds a {@link
 * Flux#retry} and there is a failure.
 *
 * <p>Additionally, the caller could create multiple concurrent subscriptions, meaning that access
 * to this shared mutable state could be concurrent, which could cause problems if the shared
 * resource is not thread safe.
 */
final class Solution_6 {
    private Service service;

    /**
     * Do not instantiate mutable state eagerly, use {@link Flux#defer} so that each subscription
     * creates a new {@link Flux}, each having it's own fresh instance.
     */
    Flux<Integer> solution_1() {
        return Flux.defer(
                () -> {
                    AtomicInteger count = new AtomicInteger();
                    return service.findAll()
                            .map(Integer::valueOf)
                            .doOnNext(count::addAndGet)
                            .doOnComplete(() -> System.out.println("Total: " + count.get()));
                });
    }

    /**
     * The deferring effect can be achieved with other creators, for example {@link
     * Mono#fromSupplier} or {@link Mono#fromCallable} can also help solve this problem.
     */
    Flux<Integer> solution_2() {
        return Mono.fromSupplier(AtomicInteger::new)
                .flatMapMany(
                        count ->
                                service.findAll()
                                        .map(Integer::valueOf)
                                        .doOnNext(count::addAndGet)
                                        .doOnComplete(() -> System.out.println("Total: " + count.get())));
    }

    interface Service {
        Flux<String> findAll();
    }
}
