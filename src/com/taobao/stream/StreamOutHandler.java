package com.taobao.stream;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
public interface StreamOutHandler<T> {
  void onPush(T value);
}
