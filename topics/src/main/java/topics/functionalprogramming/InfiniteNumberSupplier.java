package topics.functionalprogramming;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Create a supplier that returns integer values from 0 to infinity.
 *
 * After first get() invocation a supplier must return 0. After the second one, it must return 1,
 * the next one returns 2, 3, etc.
 * Also, it should be possible to use separate suppliers simultaneously.
 */
public class InfiniteNumberSupplier {

    public static Supplier<Integer> getInfiniteRange() {
        return new Supplier<>() {
            private int count = 0;
            @Override
            public Integer get() {
                return count++;
            }
        };
    }

    // also a nice solution:
    public static Supplier<Integer> getInfiniteRangeAlt() {
        return new AtomicInteger()::getAndIncrement;
    }
}
