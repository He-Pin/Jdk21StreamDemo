package com.taobao.stream;

import java.util.Iterator;
import java.util.function.Function;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
class MapConcat<T, R> extends Stream<R> implements StreamOutHandler<T> {
    private final Stream<T> upstream;
    private final Function<T, Iterable<R>> f;

    protected Iterator<R> currentIterator;

    protected StreamOutHandler<R> downstream;

    public MapConcat(Stream<T> upstream, Function<T, Iterable<R>> f) {
        this.upstream = upstream;
        this.f = f;
    }

    @Override
    boolean tryPull(StreamOutHandler<R> collector) {
        try {
            downstream = collector;
            if (currentIterator != null) {
                if (currentIterator.hasNext()) {
                    collector.onPush(currentIterator.next());
                    return true;
                } else {
                    return upstream.tryPull(this) || currentIterator.hasNext();
                }
            } else {
                return upstream.tryPull(this) || currentIterator.hasNext();
            }
        } finally {
            downstream = null;
        }
    }

    @Override
    public void onPush(T value) {
        var coll = f.apply(value);
        currentIterator = coll.iterator();
        if (currentIterator.hasNext()) {
            downstream.onPush(currentIterator.next());
        }
    }
}
