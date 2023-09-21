package com.taobao.stream;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
public class AsyncIdentityFlow<T> extends Stream<T> {
    private final Stream<T> upstream;
    private final ReentrantLock lock = new ReentrantLock();

    public AsyncIdentityFlow(Stream<T> upstream) {
        this.upstream = upstream;
    }

    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    boolean tryPull(StreamOutHandler<T> collector) {
        final CompletableFuture<Boolean> promise = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                lock.lock();
                promise.complete(upstream.tryPull(collector));
            } finally {
                lock.unlock();
            }
        });
        return promise.join();
    }
}
