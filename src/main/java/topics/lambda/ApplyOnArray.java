package topics.lambda;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;

/**
 * a method that applies the given function to all elements of the input array.
 * Does not create a new array, just changes the elements of the original one.
 */
public class ApplyOnArray {

    public static <T> void applyFunction(T[] array, Function<T, T> func) {
        // write your code here
        for (int i = 0; i < array.length; i++) {
            array[i] = func.apply(array[i]);
        }
        // also works:
        Arrays.setAll(array, t -> func.apply(array[t]));
    }

    // do not change code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String method = scanner.nextLine();
        String[] array = scanner.nextLine().split(" ");
        applyFunction(array,
                "lower".equals(method) ? String::toLowerCase :
                        "upper".equals(method) ? String::toUpperCase :
                                "new".equals(method) ? String::new :
                                        "trim".equals(method) ? String::trim : String::intern);
        Arrays.stream(array).forEach(e -> System.out.print(e + " "));
    }
}
