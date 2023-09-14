package com.taobao.stream;

import java.util.function.Predicate;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
class FilterFlow<T> extends OneToOneFlow<T, T> {
  private final Predicate<T> predicate;

  public FilterFlow(Stream upstream, Predicate<T> predicate) {
    super(upstream);
    this.predicate = predicate;
  }

  @Override
  protected T transform(T input) {
    if (predicate.test(input)) {
      return input;
    }
    return null;
  }
}
