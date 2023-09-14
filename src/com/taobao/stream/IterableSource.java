package com.taobao.stream;

import java.util.Iterator;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
class IterableSource<T> extends Source<T> {
    private final Iterable<T> iterable;
    private Iterator<T> currentIterator;

    public IterableSource(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    protected void onPull(StreamOutHandler<T> collector) {
        if (currentIterator == null) {
            currentIterator = iterable.iterator();
        }
        if (currentIterator.hasNext()) {
            collector.onPush(currentIterator.next());
        }
        if (!currentIterator.hasNext()) {
            complete();
        }
    }
}
