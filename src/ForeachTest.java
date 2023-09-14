import com.taobao.stream.Stream;

import java.util.List;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class ForeachTest {
    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4, 5);
        Stream.fromIterable(list)
            .filter(i -> i % 2 == 1)
            .map(String::valueOf)
            .foreachAsync(System.out::println)
            .toCompletableFuture()
            .join();
    }
}
