package topics.threads;

import java.util.Arrays;

class ThreadUtil {
    static void printNameOfTerminatedThread(Thread[] threads) {
        // implement the method
        Arrays.stream(threads)
                .filter(thread -> thread.getState().equals(Thread.State.TERMINATED))
                .map(Thread::getName).forEach(System.out::println);
    }
}