package com.ickstream.protocol.backend.core;

import com.ickstream.protocol.service.corebackend.CoreBackendService;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class ServiceFactory {
    private static final String COREBACKENDSERVICE_ENDPOINT = "http://api.ickstream.com/ickstream-cloud-core/backend/jsonrpc";

    private static String getBackendEndpoint() {
        String url = System.getProperty("ickstream-core-backend-url");
        if (url != null) {
            return url;
        } else {
            return COREBACKENDSERVICE_ENDPOINT;
        }
    }

    private static HttpClient createHttpClient() {
        try {
            Class.forName("org.apache.http.impl.conn.PoolingClientConnectionManager");
            return new DefaultHttpClient(new PoolingClientConnectionManager());
        } catch (ClassNotFoundException e) {
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            return new DefaultHttpClient(new ThreadSafeClientConnManager(client.getParams(), registry), client.getParams());
        }
    }

    public static CoreBackendService getCoreBackendService(String accessToken) {
        CoreBackendServiceImpl coreService = new CoreBackendServiceImpl(createHttpClient(), getBackendEndpoint());
        coreService.setAccessToken(accessToken);
        return coreService;
    }
}
