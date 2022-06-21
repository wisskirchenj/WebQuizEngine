package topics.threads;

import java.util.Arrays;

class ThreadProcessor {
    public static void findAndStartThread(Thread... threads) throws InterruptedException {
        Thread thread = Arrays.stream(threads).filter(t -> t.getState() == Thread.State.NEW)
                .findFirst().get();
        thread.start();
        thread.join();
    }
}

