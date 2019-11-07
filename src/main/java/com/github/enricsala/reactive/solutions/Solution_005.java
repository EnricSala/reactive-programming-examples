package com.github.enricsala.reactive.solutions;

import reactor.core.publisher.Mono;

/**
 * In this case we want to execute a side-effect, but we also want to return the original element.
 * The solution is not to use a terminal operation at that point, because {@link Mono#subscribe}
 * could create a detached subscription where we miss errors plus we cannot cancel it, and similarly
 * a {@link Mono#block} cannot be cancelled.
 */
final class Solution_005 {
    private Service service;

    /** Use {@link Mono#flatMap} with {@link Mono#thenReturn} to implement this. */
    Mono<Void> solution() {
        return service.create("foo")
                .flatMap(str -> service.update(str).thenReturn(str.length()))
                .doOnNext(len -> System.out.println("Length: " + len))
                .then();
    }

    interface Service {
        Mono<String> create(String foo);
        Mono<Void> update(String foo);
    }
}
