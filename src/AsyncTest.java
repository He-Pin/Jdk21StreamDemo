import com.taobao.stream.Stream;

import java.util.stream.IntStream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class AsyncTest {
    public static void main(String[] args) throws Exception {
        Stream.fromJavaStream(IntStream.range(1, 100))
            .filter(i -> i % 2 == 1)
            .async()
            .map(String::valueOf)
            .foreachAsync(System.out::println)
            .toCompletableFuture()
            .join();

        Thread.sleep(1000);
    }
}
