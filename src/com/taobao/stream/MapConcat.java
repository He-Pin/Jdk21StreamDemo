package com.taobao.stream;

import java.util.Iterator;
import java.util.function.Function;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
class MapConcat<T, R> extends Stream<R> implements StreamCollector<T> {
    private final Stream<T> upstream;
    private final Function<T, Iterable<R>> f;

    protected Iterator<R> currentIterator;

    protected StreamCollector<R> downstream;

    public MapConcat(Stream<T> upstream, Function<T, Iterable<R>> f) {
        this.upstream = upstream;
        this.f = f;
    }

    @Override
    boolean collect(StreamCollector<R> collector) {
        try {
            downstream = collector;
            if (currentIterator != null) {
                if (currentIterator.hasNext()) {
                    collector.emit(currentIterator.next());
                    return true;
                } else {
                    return upstream.collect(this) || currentIterator.hasNext();
                }
            } else {
                return upstream.collect(this) || currentIterator.hasNext();
            }
        } finally {
            downstream = null;
        }
    }

    @Override
    public void emit(T value) {
        var coll = f.apply(value);
        currentIterator = coll.iterator();
        if (currentIterator.hasNext()) {
            downstream.emit(currentIterator.next());
        }
    }
}
