package com.taobao.stream;


/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
abstract class OneToOneFlow<T, R> extends Stream<R> implements StreamOutHandler<T> {
  private final Stream<T> upstream;

  protected StreamOutHandler<R> downstream;

  public OneToOneFlow(Stream<T> upstream) {
    this.upstream = upstream;
  }

  protected abstract R transform(T input);

  @Override
  public void onPush(T value) {
    final R r = transform(value);
    if (r != null) {
      downstream.onPush(r);
    }
  }

  @Override
  boolean tryPull(StreamOutHandler<R> collector) {
    try {
      downstream = collector;
      return upstream.tryPull(this);
    } finally {
      downstream = null;
    }
  }
}
