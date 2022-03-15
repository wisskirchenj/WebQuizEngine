package topics.optional;

import java.util.Optional;
import java.util.Scanner;

/**
 * a program with a class representing an address book. The class provides a method that returns the Optional
 * address of a person by its name. If the name is not present in the book, the method returns an empty Optional.
 *
 * You need to complete the code to print the person's name and its address (separated by the phrase lives at)
 * if the address is present and the "Unknown" string otherwise.
 */
public class AddressPrinter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();
        Optional<String> optAddress = AddressBook.getAddressByName(name);

        optAddress.ifPresentOrElse(
                address -> System.out.println(name + " lives at " + address), // Consumer
                () -> System.out.println("Unknown") // Runnable
        );
    }
}

