import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("usage: ClientMain <host> <port> <name> [--delay N] [--abort]");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String name = args[2];
        int delay = 0;
        boolean abort = false;

        for (int i = 3; i < args.length; i++) {
            if ("--delay".equals(args[i]) && i + 1 < args.length) {
                delay = Integer.parseInt(args[++i]);
            } else if ("--abort".equals(args[i])) {
                abort = true;
            }
        }

        try (Socket sock = new Socket(host, port)) {
            OutputStream out = sock.getOutputStream();
            out.write(name.getBytes(StandardCharsets.US_ASCII));
            out.write(0);
            out.flush();

            if (abort) {
                System.out.println("aborting before reading response");
                return;
            }

            if (delay > 0) {
                Thread.sleep(delay * 1000L);
            }

            InputStream in = sock.getInputStream();
            DataInputStream din = new DataInputStream(in);

            byte[] priv = readBlock(din);
            byte[] cert = readBlock(din);

            try (FileOutputStream f1 = new FileOutputStream(name + ".key")) {
                f1.write(priv);
            }
            try (FileOutputStream f2 = new FileOutputStream(name + ".crt")) {
                f2.write(cert);
            }

            System.out.println("saved " + name + ".key and " + name + ".crt");
        }
    }

    private static byte[] readBlock(DataInputStream in) throws IOException {
        int len = in.readInt();
        byte[] b = new byte[len];
        in.readFully(b);
        return b;
    }
}