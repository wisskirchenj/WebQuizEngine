package topics.defaultmethods;

interface Notifier {
    default void greeting() {
        System.out.println("Notifier is ready");
    }
}
