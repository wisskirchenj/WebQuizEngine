package topics.functionalinterface;

/**
 * a lambda expression that accepts seven (!) string arguments and returns a string in
 * uppercase concatenated from all of them (in the order of arguments). (or varargs alternative..)
 */
class Seven {
    public static SeptenaryStringFunction fun =
            (s1, s2, s3, s4, s5, s6, s7) -> (s1 + s2 + s3 + s4 + s5 + s6 + s7).toUpperCase();

    public static SeptenaryStringF2 fun2 = str -> String.join("", str).toUpperCase();
}
