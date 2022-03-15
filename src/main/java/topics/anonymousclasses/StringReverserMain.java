package topics.anonymousclasses;

import java.util.Scanner;

/**
 * an anonymous class that implements the interface and assign the instance to the variable reverser.
 * The anonymous class overrides the method reverse of the interface. It returns the reversed input string.
 */
public class StringReverserMain {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        StringReverser reverser = new StringReverser() {
            @Override
            public String reverse(String str) {
                return new StringBuilder(str).reverse().toString();
            }
        };

        System.out.println(reverser.reverse(line));
    }
}
