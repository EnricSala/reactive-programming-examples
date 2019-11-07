package com.github.enricsala.reactive.solutions;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * We are implementing a long-running task that responds to events and saves the emitted elements so
 * that we can later retrieve them. We do not want the task to stop, so the subscription should not
 * terminate, but {@link Repository#save} could fail, which would cause the error signal to be
 * propagated and this would stop the subscription.
 *
 * <p>At that point the only option would be to start the task again, but if this task is started on
 * application startup then it would require restarting the application. This can be the case for
 * example if we start the task in a handler of a life cycle event of an application.
 */
final class Solution_10 {
    private Service service;
    private Repository repository;

    /**
     * Drop any potential errors from the inner {@link Publisher} to make sure that the subscription
     * does not terminate. Could also add a {@link Flux#retry} on the main flow in case {@link
     * Service#observe} emits an error, but in this case we are assuming this infinite {@link Flux}
     * does not fail.
     */
    void startLongRunningTask() {
        service.observe()
                .switchMap(content -> repository.save(content).onErrorResume(t -> Mono.empty()))
                .subscribeOn(Schedulers.elastic())
                .doOnSubscribe(unused -> System.out.println("Starting..."))
                .subscribe();
    }

    interface Service {
        /** Returns an infinite stream that randomly emits items. */
        Flux<Object> observe();
    }

    interface Repository {
        Mono<Void> save(Object content);
        Mono<Object> getLatest();
    }
}
