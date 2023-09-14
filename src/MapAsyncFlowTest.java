import com.taobao.stream.Stream;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * TODO: description of this file
 *
 * @author 虎鸣, hepin.p@alibaba-inc.com
 */
public class MapAsyncFlowTest {
    public static void main(String[] args) {
        Stream.fromJavaStream(IntStream.range(1, 50))
            .mapAsyncUnordered(10, i -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Throwable e) {
                    //
                }
                return i;
            })
//            .filter(i -> i % 2 == 1)
            .foreachAsync(System.out::println)
            .toCompletableFuture()
            .join();
    }
}
