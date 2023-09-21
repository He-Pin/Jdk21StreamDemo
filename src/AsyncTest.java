import com.taobao.stream.Stream;

import java.util.stream.IntStream;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
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
