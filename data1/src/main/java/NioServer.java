import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.*;

public class NioServer {
    private final int port;
    private final BlockingQueue<KeyRequest> genQueue;
    private final BlockingQueue<KeyResult> sendQueue;

    public NioServer(int port, BlockingQueue<KeyRequest> genQueue, BlockingQueue<KeyResult> sendQueue) {
        this.port = port;
        this.genQueue = genQueue;
        this.sendQueue = sendQueue;
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();

        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

        Thread sender = new Thread(this::sendLoop);
        sender.setDaemon(true);
        sender.start();

        System.out.println("server listening on " + port);

        while (true) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isAcceptable()) {
                    SocketChannel ch = server.accept();

                    ch.configureBlocking(false);
                    ch.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    readName(key);
                }
            }
        }
    }

    private void readName(SelectionKey key) {
        SocketChannel ch = (SocketChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(256);

        try {
            int n = ch.read(buf);
            if (n == -1) {
                ch.close();
                return;
            }

            if (n > 0) {
                buf.flip();
                byte[] data = new byte[buf.remaining()];
                buf.get(data);
                int zero = -1;

                for (int i = 0; i < data.length; i++) {
                    if (data[i] == 0) {
                        zero = i;
                        break;
                    }
                }

                if (zero >= 0) {
                    String name = new String(data, 0, zero);

                    genQueue.offer(new KeyRequest(name, ch));
                    key.cancel();
                }
            }
        } catch (IOException e) {
            try {
                ch.close();
            } catch (IOException ignored) {}
        }
    }

    private void sendLoop() {
        while (true) {
            try {
                KeyResult res = sendQueue.take();
                ByteBuffer buf = ByteBuffer.wrap(res.getPayload());

                while (buf.hasRemaining()) {
                    res.getChannel().write(buf);
                }

                res.getChannel().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}