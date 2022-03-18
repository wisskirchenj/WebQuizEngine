package topics.optional;

import java.util.Optional;
import java.util.Scanner;

/**
 * the method getValue of the InputStringReader class. It should read a String value from the console and
 * construct Optional<String> object based on the value. If an input String equals empty, then create an empty Optional.
 */
class InputStringReader {
    public Optional<String> getValue() {
        String input = new Scanner(System.in).nextLine();
        return Optional.ofNullable("empty".equals(input) ? null : input);
    }
}
