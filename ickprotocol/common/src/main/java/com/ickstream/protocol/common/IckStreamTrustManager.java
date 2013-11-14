package com.ickstream.protocol.common;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * X509TrustManager implementation that allows access to ickStream cloud services using https
 * <p/>
 * This is just a workaround to the fact that all necessary CA certificates aren't included in the default Java distribution
 * from Oracle. They are included on OSX and Android, so it's mainly an issue for Linux users.
 */
public class IckStreamTrustManager implements X509TrustManager {
    private static final String JAVA_CA_CERT_FILE_NAME = "cacerts";
    private static final String CLASSIC_JAVA_CA_CERT_FILE_NAME = "jssecacerts";

    private KeyStore certificateTrustStore;
    private X509TrustManager defaultTrustManager;

    private static SSLContext context;

    /**
     * Initialize SSL trust manager and ensure that we are allowed to do https access to api.ickstream.com
     * This method will use the default password to access the Java cacerts keystore
     */
    public static void init() {
        init("changeit");
    }

    /**
     * Initialize SSL trust manager and ensure that we are allowed to do https access to api.ickstream.com
     *
     * @param caCertsKeyStorePassword The password to the Java cacerts keystore
     */
    public static void init(final String caCertsKeyStorePassword) {
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new IckStreamTrustManager(caCertsKeyStorePassword)}, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the SSL context to use or null if the ickStream SSL trust manager hasn't been initialized
     *
     * @return The SSL context to use
     */
    public static SSLContext getContext() {
        return context;
    }

    private IckStreamTrustManager(final String caCertsKeyStorePassword) {
        try {
            initTrustStore(caCertsKeyStorePassword);
            addTrustedCerts();
            initDefaultTrustManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        defaultTrustManager.checkClientTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        defaultTrustManager.checkServerTrusted(chain, authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return defaultTrustManager.getAcceptedIssuers();
    }

    private void initTrustStore(String caCertsKeystorePassword) throws Exception {
        File javaTrustStoreFile = findJavaTrustStoreFile();
        InputStream inputStream = new FileInputStream(javaTrustStoreFile);
        certificateTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        certificateTrustStore.load(inputStream, caCertsKeystorePassword.toCharArray());
        inputStream.close();
    }

    private void addTrustedCerts() throws Exception {
        certificateTrustStore.setCertificateEntry("startcom.ca", CertificateFactory.getInstance("X.509").generateCertificate(getClass().getResourceAsStream("/com/ickstream/certs/ca.pem")));
        certificateTrustStore.setCertificateEntry("startcom.ca.sub.class2", CertificateFactory.getInstance("X.509").generateCertificate(getClass().getResourceAsStream("/com/ickstream/certs/sub.class2.server.ca.pem")));
    }

    private void initDefaultTrustManager() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(certificateTrustStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                defaultTrustManager = (X509TrustManager) trustManager;
                break;
            }
        }
    }

    private File findJavaTrustStoreFile() {
        File javaHome = new File(System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security");
        File caCertsFile = new File(javaHome, JAVA_CA_CERT_FILE_NAME);
        if (!caCertsFile.exists() || !caCertsFile.isFile()) {
            caCertsFile = new File(javaHome, CLASSIC_JAVA_CA_CERT_FILE_NAME);
        }
        return caCertsFile;
    }
}
