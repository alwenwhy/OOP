import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.*;

public class CryptoWorker implements Runnable {
    private final BlockingQueue<KeyRequest> genQueue;
    private final BlockingQueue<KeyResult> sendQueue;
    private final PrivateKey signKey;
    private final String issuer;

    static final Map<String, byte[]> cache = new ConcurrentHashMap<>();

    public CryptoWorker(BlockingQueue<KeyRequest> genQueue, BlockingQueue<KeyResult> sendQueue, PrivateKey signKey, String issuer) {
        this.genQueue = genQueue;
        this.sendQueue = sendQueue;
        this.signKey = signKey;
        this.issuer = issuer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                KeyRequest req = genQueue.take();
                String name = req.getName();

                byte[] payload = cache.get(name);
                if (payload == null) {
                    payload = generate(name);
                    cache.put(name, payload);
                }

                sendQueue.offer(new KeyResult(req.getChannel(), payload));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] generate(String name) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(8192);
        KeyPair kp = kpg.generateKeyPair();

        X509Certificate cert = CertUtil.buildCert(kp, name, issuer, signKey);

        byte[] priv = kp.getPrivate().getEncoded();
        byte[] pub = kp.getPublic().getEncoded();
        byte[] certBytes = cert.getEncoded();

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        writeBlock(out, priv);
        writeBlock(out, certBytes);
        writeBlock(out, pub);

        return out.toByteArray();
    }

    private static void writeBlock(java.io.ByteArrayOutputStream out, byte[] b) throws java.io.IOException {
        out.write((b.length >> 24) & 0xff);
        out.write((b.length >> 16) & 0xff);
        out.write((b.length >> 8) & 0xff);
        out.write(b.length & 0xff);
        out.write(b);
    }
}