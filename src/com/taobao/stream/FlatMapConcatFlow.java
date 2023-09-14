package com.taobao.stream;

import java.util.function.Function;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class FlatMapConcatFlow<T, R> extends Stream<R> implements StreamCollector<T> {
    private final Stream<T> upstream;
    private final Function<T, Stream<R>> f;

    private Stream<R> currentStream;
    protected StreamCollector<R> downstream;

    private boolean upstreamHasMore = true;

    public FlatMapConcatFlow(Stream<T> upstream, Function<T, Stream<R>> f) {
        this.upstream = upstream;
        this.f = f;
    }

    @Override
    boolean collect(StreamCollector<R> collector) {
        try {
            downstream = collector;
            if (currentStream != null) {
                return currentStream.collect(collector) || switchToNext(collector);
            } else {
                return switchToNext(collector);
            }
        } finally {
            downstream = null;
        }
    }

    private boolean switchToNext(StreamCollector<R> collector) {
        if (!upstreamHasMore) {
            return false;
        }
        upstreamHasMore = upstream.collect(this);
        return true;
    }

    @Override
    public void emit(T value) {
        currentStream = f.apply(value);
    }
}
