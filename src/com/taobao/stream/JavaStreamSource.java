package com.taobao.stream;

import java.util.Iterator;
import java.util.stream.BaseStream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
class JavaStreamSource<T> extends Source<T> {
    private final BaseStream<T, ? extends BaseStream<T, ?>> javaStream;
    private Iterator<T> currentIterator;

    public JavaStreamSource(BaseStream<T, ? extends BaseStream<T, ?>> javaStream) {
        this.javaStream = javaStream;
    }

    @Override
    void close()  {
        javaStream.close();
    }

    @Override
    protected void onPull(StreamOutHandler<T> collector) {
        if (currentIterator == null) {
            currentIterator = javaStream.iterator();
        }
        if (currentIterator.hasNext()) {
            collector.onPush(currentIterator.next());
        }
        if (!currentIterator.hasNext()) {
            complete();
        }
    }
}
