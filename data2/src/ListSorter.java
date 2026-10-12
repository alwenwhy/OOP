public final class ListSorter {
    private final MyList list;
    private final long stepDelayMs;
    private final long tickDelayMs;
    private volatile boolean running = true;

    public ListSorter(MyList list, long stepDelayMs, long tickDelayMs) {
        this.list = list;
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
        Node cur = list.getHead();

        if (cur == null) {
            sleep(stepDelayMs);
            return;
        }

        while (running) {
            Node next = doStep(cur);
            if (next == null)
                break;

            cur = next;
        }
    }

    private Node doStep(Node a) {
        if (a == null || a.next == null)
            return null;

        Node b = a.next;
        int r = 0;
        while (running) {
            r = trySwap(a, b);
            if (r != 0)
                break;

            sleep(tickDelayMs);
        }
        if (r == 0)
            return null;

        sleep(stepDelayMs);

        return (r == 2) ? a : b;
    }

    private int trySwap(Node a, Node b) {
        if (a == null || b == null || (a.next != b && b.next != a))
            return 1;

        Node first = a, second = b;
        if (b.prev == a) {
            first = a;
            second = b;
        } else if (a.prev == b) {
            first = b;
            second = a;
        } else
            return 1;

        if (!first.lock.tryLock())
            return 0;

        try {
            if (!second.lock.tryLock())
                return 0;

            try {
                if (first.next != second)
                    return 1;   // устарело
                if (first.value.compareTo(second.value) > 0) {
                    list.swapNodes(first, second);
                    return 2;
                }

                return 1;
            } finally {
                second.lock.unlock();
            }
        } finally {
            first.lock.unlock();
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}