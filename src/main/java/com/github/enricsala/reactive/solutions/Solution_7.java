package com.github.enricsala.reactive.solutions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * There are three problems in this case, all related to resubscription:
 *
 * <p>First, the {@link Mono#doFinally} will close the {@link InputStream} the first time that it
 * is invoked, so subsequent subscriptions would find that it has already been closed.
 *
 * <p>Second, there could be a failure during the subscription of {@link Service#upload} and the
 * {@link Mono#retry} would try to execute it again, but an {@link InputStream} is mutable and
 * any reads will consume its data. Thus, subsequent subscriptions could find that the {@link
 * InputStream} has already been consumed half-way, or find that its end has been reached.
 *
 * <p>Third, the caller could create multiple concurrent subscriptions, which could compete to
 * consume data from the {@link InputStream}.
 *
 * <p>In conclusion, {@link Publisher}s must be safe to resubscribe, so mutable state should be
 * avoided and always deferred so that each subscription gets its own instance.
 */
final class Solution_7 {
    private Service service;

    /**
     * Make sure that each subscription operates on a fresh {@link InputStream} using {@link
     * Mono#fromSupplier} to create an instance when required.
     */
    Mono<Void> solution_1(byte[] data) {
        return Mono.fromSupplier(() -> new ByteArrayInputStream(data))
                .flatMap(
                        input ->
                                service.upload(input)
                                        .doFinally(unused -> close(input)))
                .retry(5);
    }

    /**
     * Use {@link Mono#using} which provides handlers for creating the instance and closing it,
     * thus being very helpful when it's necessary to release resources.
     */
    Mono<Void> solution_2(byte[] data) {
        return Mono.using(() -> new ByteArrayInputStream(data), service::upload, this::close)
                .retry(5);
    }

    private void close(InputStream inputStream) {
        // close the stream
    }

    interface Service {
        Mono<Void> upload(InputStream inputStream);
    }
}
