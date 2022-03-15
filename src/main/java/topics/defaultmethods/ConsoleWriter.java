package topics.defaultmethods;

/**
 * Resolve a diamond problem of ConsoleWriter class. Override greeting method according to Printer
 * interface implementation. Try to avoid code duplication.
 */
class ConsoleWriter implements Printer, Notifier {
    @Override
    public void greeting() {
        Printer.super.greeting();
    }
}

