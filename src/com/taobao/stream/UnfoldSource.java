package com.taobao.stream;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
class UnfoldSource<S, T> extends Source<T> {
    private final Supplier<S> creator;

    private final Consumer<S> closeFunction;

    private final Function<S, T> f;

    private S state;

    public UnfoldSource(Supplier<S> creator, Function<S, T> f, Consumer<S> closeFunction) {
        this.creator = creator;
        this.closeFunction = closeFunction;
        this.f = f;
    }

    @Override
    void close() throws Exception {
        if (closeFunction != null) {
            closeFunction.accept(state);
        }
    }

    @Override
    protected void onPull(StreamCollector<T> collector) {
        if (state == null) {
            state = Objects.requireNonNull(creator.get());
        }
        var result = f.apply(state);
        if (result != null) {
            collector.emit(result);
        } else {
            complete();
        }
    }
}
