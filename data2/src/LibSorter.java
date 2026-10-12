import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public final class LibSorter {
    private final List<String> list;
    private final ReentrantLock lock;
    private final long stepDelayMs;
    private final long tickDelayMs;
    private volatile boolean running = true;

    public LibSorter(List<String> list, ReentrantLock lock, long stepDelayMs, long tickDelayMs) {
        this.list = list;
        this.lock = lock;
        this.stepDelayMs = stepDelayMs;
        this.tickDelayMs = tickDelayMs;
    }

    public void stop() {
        running = false;
    }

    public void bubbleForever() {
        while (running) {
            bubbleOnce();
        }
    }

    private void bubbleOnce() {
        int i = 0;
        while (running) {
            int n = size();
            if (i >= n - 1)
                break;

            while (running) {
                if (trySwap(i))
                    break;

                sleep(tickDelayMs);
            }
            sleep(stepDelayMs);

            i++;
        }
    }

    private int size() {
        lock.lock();

        try {
            return list.size();
        } finally {
            lock.unlock();
        }
    }

    private boolean trySwap(int i) {
        if (!lock.tryLock())
            return false;

        try {
            if (i + 1 >= list.size())
                return true;

            String a = list.get(i);
            String b = list.get(i + 1);
            if (a.compareTo(b) > 0) {
                list.set(i, b);
                list.set(i + 1, a);
            }

            return true;
        } finally {
            lock.unlock();
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}