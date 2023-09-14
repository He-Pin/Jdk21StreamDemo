import com.taobao.stream.Stream;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class UnfoldTest {
    public static void main(String[] args) {
        Stream.unfold(() -> new AtomicInteger(0),
                (counter) -> {
                    if (counter.get() < 10) {
                        return counter.incrementAndGet();
                    } else {
                        return null;
                    }
                })
            .filter(i -> i % 2 == 1)
            .map(String::valueOf)
            .foreachAsync(System.out::println)
            .toCompletableFuture()
            .join();
    }
}
