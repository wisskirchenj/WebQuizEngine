package topics.optional;

import java.util.Optional;

/**
 * code that sums up all values and prints the result. If Optional object is empty, just skip it.
 */
public class OptionalListSum {
    public static void main(String[] args) {
        IntListProvider provider = new IntListProvider();

        System.out.println(provider.getValues().stream()
                .filter(Optional::isPresent).mapToInt(Optional::get).sum());
    }
}

