import com.taobao.stream.Stream;

import java.util.stream.IntStream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class JavaStreamTest {
    public static void main(String[] args) {
        Stream.fromJavaStream(IntStream.range(1, 5))
            .filter(i -> i % 2 == 1)
            .map(String::valueOf)
            .foreachAsync(System.out::println)
            .toCompletableFuture()
            .join();
    }
}
