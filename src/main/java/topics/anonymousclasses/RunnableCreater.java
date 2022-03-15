package topics.anonymousclasses;

public class RunnableCreater {

    /**
     * the given method  takes two arguments: text and repeats. The method returns an instance of an anonymous class
     * implementing java.lang.Runnable. The overridden method of the anonymous class should print the text to the
     * standard output a specified number of times (repeats).
     * @param text text to print
     * @param repeats how often the text is printed
     * @return an instance of an anonymous class implementing java.lang.Runnable
     */
    public static Runnable createRunnable(String text, int repeats) {
        return new Runnable() {
            @Override
            public void run() {
                System.out.print((text+"\n").repeat(repeats));
            }
        };
    }
}
