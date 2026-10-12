import java.util.Iterator;
import java.util.NoSuchElementException;

public final class MyList implements Iterable<String> {
    private Node head;
    private Node tail;

    private final Object structLock = new Object();

    public void addFirst(String value) {
        Node n = new Node(value);

        synchronized (structLock) {
            if (head == null) {
                head = n;
                tail = head;
            } else {
                n.next = head;
                head.prev = n;
                head = n;
            }
        }
    }

    public Node getHead() {
        synchronized (structLock) {
            return head;
        }
    }

    public void swapNodes(Node a, Node b) {
        Node pa = a.prev;
        Node nb = b.next;

        b.prev = pa;
        b.next = a;
        a.prev = b;
        a.next = nb;

        if (pa != null)
            pa.next = b;
        if (nb != null)
            nb.prev = a;

        synchronized (structLock) {
            if (head == a)
                head = b;

            if (tail == b)
                tail = a;
        }
    }

    @Override
    public Iterator<String> iterator() {
        final Node start = getHead();
        return new Iterator<>() {
            Node cur = start;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public String next() {
                if (cur == null)
                    throw new NoSuchElementException();

                String v = cur.value;
                cur = cur.next;
                return v;
            }
        };
    }
}
