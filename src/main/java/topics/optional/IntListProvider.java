package topics.optional;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * provide a list of Optional<Integer>'s with possible empty objects, when user entered "null" instead of a number.
 */
class IntListProvider {
    private List<Optional<Integer>> opts; // cache to provide reproducing method invocation

    public List<Optional<Integer>> getValues() {
        if (opts != null) {
            return opts;
        }

        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        opts = IntStream.rangeClosed(1, number).mapToObj(n -> {
                    String val = scanner.next();
                    return "null".equals(val) ?
                            Optional.<Integer>empty() :
                            Optional.of(Integer.valueOf(val));
                }).toList();

        return opts;
    }
}
