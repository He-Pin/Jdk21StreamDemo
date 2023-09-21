package com.taobao.stream;


/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
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
    try {
      final R r = transform(value);
      if (r != null) {
        downstream.onPush(r);
      }
    } catch (Throwable e) {
      e.printStackTrace();
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
