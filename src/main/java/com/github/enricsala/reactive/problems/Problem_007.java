package com.github.enricsala.reactive.problems;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import reactor.core.publisher.Mono;

final class Problem_007 {
    private Service service;

    Mono<Void> problem(byte[] data) {
        return send(new ByteArrayInputStream(data)).retry(5);
    }

    private Mono<Void> send(InputStream inputStream) {
        return service.upload(inputStream)
                .doFinally(unused -> close(inputStream));
    }

    private void close(InputStream inputStream) {
        // close the stream
    }

    interface Service {
        Mono<Void> upload(InputStream inputStream);
    }
}
