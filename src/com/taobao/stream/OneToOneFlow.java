package com.taobao.stream;


/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
abstract class OneToOneFlow<T, R> extends Stream<R> implements StreamCollector<T> {
  private final Stream<T> upstream;

  protected StreamCollector<R> downstream;

  public OneToOneFlow(Stream<T> upstream) {
    this.upstream = upstream;
  }

  protected abstract R transform(T input);

  @Override
  public void emit(T value) {
    final R r = transform(value);
    if (r != null) {
      downstream.emit(r);
    }
  }

  @Override
  boolean collect(StreamCollector<R> collector) {
    try {
      downstream = collector;
      return upstream.collect(this);
    } finally {
      downstream = null;
    }
  }
}
