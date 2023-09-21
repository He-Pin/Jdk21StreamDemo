package com.taobao.stream;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
public class MapAsyncUnorderedFlow<T, R> extends Stream<R> implements StreamOutHandler<T> {
    private static final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    private final ReentrantLock lock = new ReentrantLock();
    private final ArrayBlockingQueue<Holder<R>> queue;
    private final Stream<T> upstream;

    private final int parallelism;

    private final Function<T, R> f;

    public MapAsyncUnorderedFlow(Stream<T> upstream, int parallelism, Function<T, R> f) {
        this.upstream = upstream;
        this.parallelism = parallelism;
        this.f = f;
        this.queue = new ArrayBlockingQueue<>(parallelism);
    }

    @Override
    boolean tryPull(StreamOutHandler<R> collector) {
        try {
            queue.put(new Holder<>(collector));
            return upstream.tryPull(this) || queue.isEmpty();
        } catch (Exception e) {
            close();
            upstream.close();
            return false;
        }
    }

    @Override
    public void onPush(T value) {
        try {
            executor.execute(() -> {
                CompletableFuture<R> promise = null;
                try {
                    var r = f.apply(value);
                    promise = queue.take().promise;
                    promise.complete(r);
                } catch (Exception e) {
                    if (promise != null) {
                        promise.completeExceptionally(e);
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private class Holder<R> {
        private final CompletableFuture<R> promise = new CompletableFuture<>();

        public Holder(StreamOutHandler<R> collector) {
            promise.whenComplete((r, throwable) -> {
//                System.out.println(Thread.currentThread());
                if (r != null) {
                    try {
                        lock.lock();
                        collector.onPush(r);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    throwable.printStackTrace();
                    close();
                    upstream.close();
                }
            });
        }
    }
}
