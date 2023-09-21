package com.taobao.stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
public abstract class Sink<T> implements StreamOutHandler<T> {
  private static final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

  private final Stream<T> upstream;

  public Sink(Stream<T> upstream) {
    this.upstream = upstream;
  }

  void run() {
    for (; ; ) {
      var hasMore = upstream.tryPull(this);
      if (!hasMore) {
        return;
      }
    }
  }

  CompletionStage<Void> runAsync() {
    final CompletableFuture<Void> promise = new CompletableFuture<>();
    executor.execute(() -> {
      try{
        Sink.this.run();
        promise.complete(null);
      } catch (Throwable e) {
        promise.completeExceptionally(e);
      }
    });
    return promise;
  }


}
