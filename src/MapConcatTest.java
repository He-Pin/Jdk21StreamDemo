import com.taobao.stream.Stream;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: description of this file
 *
 * @author hepin, hepin1989@gmail.com
 */
public class MapConcatTest {
    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4, 5);
        Stream.fromIterable(list)
            .mapConcat(i ->
                java.util.stream.Stream.generate(() -> i)
                    .limit(i)
                    .collect(Collectors.toList()))
            .map(String::valueOf)
            .foreachAsync(System.out::println)
            .toCompletableFuture()
            .join();
    }
}
