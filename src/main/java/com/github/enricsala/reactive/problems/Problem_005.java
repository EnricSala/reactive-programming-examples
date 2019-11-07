package com.github.enricsala.reactive.problems;

import reactor.core.publisher.Mono;

final class Problem_005 {
    private Service service;

    Mono<Void> problem() {
        return service.create("foo")
                .map(
                        str -> {
                            service.update(str).subscribe();
                            return str.length();
                        })
                .doOnNext(len -> System.out.println("Length is: " + len))
                .then();
    }

    interface Service {
        Mono<String> create(String foo);
        Mono<Void> update(String foo);
    }
}
