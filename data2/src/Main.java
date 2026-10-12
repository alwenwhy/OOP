import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    enum Impl { LIST, LIB }

    static final long STEP_DELAY_MS = 200;
    static final long TICK_DELAY_MS = 50;
    static final int  THREADS = 3;
    static final Impl impl = Impl.LIST;

    static final MyList myList = new MyList();
    static ListSorter listSorter;

    static final List<String> libList = Collections.synchronizedList(new ArrayList<>());
    static final ReentrantLock libLock = new ReentrantLock();
    static LibSorter libSorter;

    public static void main(String[] args) {
        System.out.println("impl = " + impl);

        if (impl == Impl.LIST) {
            listSorter = new ListSorter(myList, STEP_DELAY_MS, TICK_DELAY_MS);

            for (int i = 0; i < THREADS; i++) {
                Thread t = new Thread(listSorter::bubbleForever, "sorter-" + i);
                t.setDaemon(true);
                t.start();
            }
        } else {
            libSorter = new LibSorter(libList, libLock, STEP_DELAY_MS, TICK_DELAY_MS);

            for (int i = 0; i < THREADS; i++) {
                Thread t = new Thread(libSorter::bubbleForever, "sorter-" + i);
                t.setDaemon(true);
                t.start();
            }
        }

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            if (line.isEmpty()) {
                printState();
                continue;
            }

            for (int i = 0; i < line.length(); i += 80) {
                String part = line.substring(i, Math.min(i + 80, line.length()));
                addFirst(part);
            }
        }

        if (listSorter != null)
            listSorter.stop();
        if (libSorter != null)
            libSorter.stop();
    }

    static void addFirst(String s) {
        if (impl == Impl.LIST) {
            myList.addFirst(s);
        } else {
            libLock.lock();
            try {
                libList.add(0, s);
            } finally {
                libLock.unlock();
            }
        }
    }

    static void printState() {
        StringBuilder sb = new StringBuilder("[ ");
        if (impl == Impl.LIST) {
            for (String s : myList) {
                sb.append('"').append(s).append("\" ");
            }
        } else {
            libLock.lock();
            try {
                for (String s : libList) {
                    sb.append('"').append(s).append("\" ");
                }
            } finally {
                libLock.unlock();
            }
        }
        sb.append(']');

        System.out.println(sb);
    }
}