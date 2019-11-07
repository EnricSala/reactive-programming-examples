package com.github.enricsala.reactive.solutions;

import reactor.core.publisher.Mono;

/**
 * Using {@link Mono#block} will subscribe and execute the action, but the downside of this solution
 * is that now we cannot cancel the inner operation. If the subscriber decides to cancel there will
 * be a cancellation signal that will cancel inner subscribers, but {@link Mono#block} cannot listen
 * to that signal and will run to completion, or until an error is thrown.
 */
final class Solution_3 {
    private Service service;

    /**
     * Use {@link Mono#flatMap} so that the inner {@link Mono} is subscribed, and cancellation
     * signals can be propagated to stop the inner.
     */
    Mono<Void> solution() {
        return service.create("foo")
                .flatMap(service::update);
    }

    interface Service {
        Mono<String> create(String foo);
        Mono<Void> update(String foo);
    }
}
