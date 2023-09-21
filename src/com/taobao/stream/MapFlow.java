package com.taobao.stream;

import java.util.function.Function;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
class MapFlow<T, R> extends OneToOneFlow<T, R> {
    private final Function<T, R> function;

    public MapFlow(Stream<T> upstream, Function<T, R> function) {
        super(upstream);
        this.function = function;
    }

    @Override
    protected R transform(T input) {
        return function.apply(input);
    }
}
