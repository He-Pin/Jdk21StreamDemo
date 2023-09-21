package com.taobao.stream;

import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
public abstract class Stream<T> {

    /**
     * return ture if has more.
     */
    abstract boolean tryPull(final StreamOutHandler<T> collector);



    void close() {

    }

    public static <S, T> Stream<T> unfold(final Supplier<S> creator, final Function<S, T> f, final Consumer<S> close) {
        return new UnfoldSource<S, T>(creator, f, close);
    }

    public static <S, T> Stream<T> unfold(final Supplier<S> creator, final Function<S, T> f) {
        return new UnfoldSource<S, T>(creator, f, null);
    }

    public static <T> Stream<T> fromIterable(final Iterable<T> iterable) {
        return new IterableSource<>(iterable);
    }

    public static <T> Stream<T> fromJavaStream(final BaseStream<T, ? extends BaseStream<T, ?>> javaStream) {
        return new JavaStreamSource<>(javaStream);
    }

    public static Stream<Integer> rangeClosed(final int start, final int end) {
        return fromJavaStream(IntStream.rangeClosed(start, end));
    }

    public <R> Stream<R> map(final Function<T, R> f) {
        return new MapFlow<T, R>(this, f);
    }

    public <R> Stream<R> flatMap(final Function<T, Stream<R>> f) {
        return new FlatMapConcatFlow<>(this, f);
    }

    public <R> Stream<R> mapAsyncUnordered(final int parallelism, final Function<T, R> f) {
        return new MapAsyncUnorderedFlow<>(this, parallelism, f);
    }

    public Stream<T> async() {
        return new AsyncIdentityFlow<>(this);
    }

    public <R> Stream<R> mapConcat(final Function<T, Iterable<R>> f) {
        return new MapConcat<>(this, f);
    }

    public Stream<T> filter(final Predicate<T> predicate) {
        return new FilterFlow<>(this, predicate);
    }

    public void foreach(final Consumer<T> consumer) {
        final ForEachSink<T> foreachSink = new ForEachSink<T>(this, consumer);
        foreachSink.run();
    }

    public CompletionStage<Void> foreachAsync(final Consumer<T> consumer) {
        final ForEachSink<T> foreachSink = new ForEachSink<T>(this, consumer);
        return foreachSink.runAsync();
    }


}
