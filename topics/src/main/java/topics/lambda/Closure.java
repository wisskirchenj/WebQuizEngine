package topics.lambda;

import java.util.function.UnaryOperator;

/**
 * a lambda expression that adds PREFIX (before) and SUFFIX (after) to its single
 * string argument; PREFIX and SUFFIX are final variables.
 * All whitespaces on both ends of the argument must be removed.
 */
public class Closure {

    public static final String PREFIX = "__pref__";
    public static final String SUFFIX = "__suff__";

    public static UnaryOperator<String> operator =
            s -> PREFIX.concat(s.trim()).concat(SUFFIX);
}
