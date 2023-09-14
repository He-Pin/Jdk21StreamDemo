import com.taobao.stream.Stream;

import java.util.List;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class FlatMapConcatTest {
    public static void main(String[] args) {
        Stream.fromIterable(List.of(1, 2, 3, 4, 5))
            .flatMap(i -> Stream.fromJavaStream(
                java.util.stream.Stream.generate(() -> i)
                    .limit(i)))
            .map(String::valueOf)
            .foreach(System.out::println);
    }
}
