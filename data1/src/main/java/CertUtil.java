import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertUtil {
    public static X509Certificate buildCert(KeyPair kp, String subject, String issuer, PrivateKey signKey) throws Exception {
        X500Name issuerName = new X500Name(issuer);
        X500Name subjectName = new X500Name("CN=" + subject);

        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date from = new Date();
        Date to = new Date(from.getTime() + 365L * 24 * 3600 * 1000);

        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuerName, serial, from, to, subjectName, kp.getPublic());
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(signKey);

        return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
    }
}