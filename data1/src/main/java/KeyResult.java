import java.nio.channels.SocketChannel;

public class KeyResult {
    private final SocketChannel channel;
    private final byte[] payload;

    public KeyResult(SocketChannel channel, byte[] payload) {
        this.channel = channel;
        this.payload = payload;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public byte[] getPayload() {
        return payload;
    }
}