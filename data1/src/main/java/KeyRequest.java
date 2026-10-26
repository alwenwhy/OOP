import java.nio.channels.SocketChannel;

public class KeyRequest {
    private final String name;
    private final SocketChannel channel;

    public KeyRequest(String name, SocketChannel channel) {
        this.name = name;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public SocketChannel getChannel() {
        return channel;
    }
}