package com.taobao.stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public abstract class Sink<T> implements StreamCollector<T> {
  private static final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

  private final Stream<T> upstream;

  public Sink(Stream<T> upstream) {
    this.upstream = upstream;
  }

  protected abstract void onPush(T value);

  @Override
  public void emit(T value) {
    onPush(value);
  }

  void run() {
    for (; ; ) {
      var hasMore = upstream.collect(this);
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
