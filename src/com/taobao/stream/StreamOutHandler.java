package com.taobao.stream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public interface StreamOutHandler<T> {
  void onPush(T value);
}
