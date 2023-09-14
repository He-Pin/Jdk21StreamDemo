package com.taobao.stream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public abstract class Source<T> extends Stream<T> {
    private boolean completed;

    protected void complete() {
        completed = true;
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onPull(final StreamCollector<T> collector);

    @Override
    boolean collect(StreamCollector<T> collector) {
        onPull(collector);
        return !completed;
    }
}
