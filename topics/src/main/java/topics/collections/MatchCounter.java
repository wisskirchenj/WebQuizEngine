package topics.collections;

import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Frequency of characters
 * Write a program that reads a sequence of characters and another character, and outputs the number representing
 * how many times this character appears in the sequence.
 */
public class MatchCounter {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        Stream<String> sequenceStream = Stream.of(scanner.nextLine().split("\\s+"));
        String match = scanner.next();
        System.out.println(sequenceStream.filter(match::equals).count());
    }
}
