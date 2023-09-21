package com.taobao.stream;

import java.util.function.Consumer;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
class ForEachSink<T> extends Sink<T> {
  private final Consumer<T> consumer;

  public ForEachSink(Stream<T> upstream, Consumer<T> consumer) {
    super(upstream);
    this.consumer = consumer;
  }

  @Override
  public void onPush(T value) {
    this.consumer.accept(value);
  }
}
