package topics.functionalprogramming;

import java.util.Random;

/**
 * main for ternaryOperator
 */
public class TernaryOperatorMain {

    public static void main(String[] args) {
        Data data = new Data( "data's key", new String[] {"some value", "other value"});
        for (int i = 0; i < 5; i++) {
            boolean rand = new Random().nextBoolean();
            System.out.println(rand);
            System.out.println(Operator.ternaryOperator(t -> rand, Data::toString, Data::getKey).apply(data));
        }
    }
}

