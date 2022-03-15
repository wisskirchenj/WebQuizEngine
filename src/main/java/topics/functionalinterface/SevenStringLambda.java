package topics.functionalinterface;

/**
 * main for Seven class implementations of method references.
 */
public class SevenStringLambda {
    public static void main(String[] args) {
        System.out.println(Seven.fun.apply("a","b","c","d","e","f","g"));
        System.out.println(Seven.fun2.apply("a","b","c","d","e","f","g"));
    }
}


