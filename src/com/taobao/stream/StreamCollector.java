package com.taobao.stream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public interface StreamCollector<T> {
  void emit(T value);
}
