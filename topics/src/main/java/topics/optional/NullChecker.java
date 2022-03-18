package topics.optional;

/**
 * a program that prints the returning value of the provider.getValue if it is not null.
 */
public class NullChecker {
    public static void main(String[] args) {
        ValueProvider provider = new ValueProvider();
        provider.getValue().ifPresent(System.out::println);
    }
}

