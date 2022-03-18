package topics.lambda;

import java.util.function.LongBinaryOperator;

/**
 * a lambda expression that accepts two long arguments as a range's borders and
 * calculates (returns) the product of all numbers in this range (inclusively).
 * It's guaranteed that 0 <= left border <= right border. If left border == right border,
 * then the result is the border.
 */
public class ProductInRange {

    public static LongBinaryOperator binaryOperator = (l, m) -> {
        long res = l;
        for (long n = l + 1; n <= m; n++) {
            res *= n;
        }
        return res;
    };
}