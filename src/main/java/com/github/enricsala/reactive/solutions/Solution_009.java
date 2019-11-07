package com.github.enricsala.reactive.solutions;

import java.util.stream.Stream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The problem is that an {@link Iterable} does not offer support for closing and releasing
 * resources, so the caller would not be unable to dispose the subscription.
 */
final class Solution_009 {
    private Service service;

    /** For example could use a {@link Stream} which supports closing. */
    Stream<Object> solution() {
        return service.findAll()
                .concatMap(service::lookup)
                .toStream();
    }

    /**
     * However, all of the steps in the flow must support closing. The following example will not
     * work because the intermediate {@link Iterable} is not able to propagate the closing.
     */
    Stream<Object> doesNotWork() {
        Iterable<Object> iterable =
                service.findAll()
                        .concatMap(service::lookup)
                        .toIterable();
        return Flux.fromIterable(iterable).toStream();
    }

    interface Service {
        Flux<String> findAll();
        Mono<Object> lookup(String foo);
    }
}
