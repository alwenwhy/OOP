import java.util.concurrent.locks.ReentrantLock;

public final class Node {
    String value;
    Node prev;
    Node next;
    final ReentrantLock lock = new ReentrantLock();

    Node(String value) {
        this.value = value;
    }
}