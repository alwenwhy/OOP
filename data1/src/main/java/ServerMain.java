import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.concurrent.*;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("usage: ServerMain <port> <workerCount> <issuerName>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        int workers = Integer.parseInt(args[1]);
        String issuer = args[2];

        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream("signer.p12")) {
            ks.load(fis, "testpassword".toCharArray());
        }
        PrivateKey signKey = (PrivateKey)ks.getKey("pivkin", "testpassword".toCharArray());

        BlockingQueue<KeyRequest> genQueue = new LinkedBlockingQueue<>();
        BlockingQueue<KeyResult> sendQueue = new LinkedBlockingQueue<>();

        ExecutorService pool = Executors.newFixedThreadPool(workers);
        for (int i = 0; i < workers; i++) {
            pool.execute(new CryptoWorker(genQueue, sendQueue, signKey, issuer));
        }

        NioServer server = new NioServer(port, genQueue, sendQueue);
        server.start();
    }
}