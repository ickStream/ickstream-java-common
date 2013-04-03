package com.ickstream.protocol.service.corebackend;

import com.ickstream.protocol.common.ServiceFactory;

public class CoreBackendServiceFactory extends ServiceFactory {
    private static final String COREBACKENDSERVICE_ENDPOINT = "http://api.ickstream.com/ickstream-cloud-core/backend/jsonrpc";

    private static String getCoreBackendServiceEndpoint() {
        String url = System.getProperty("ickstream-core-backend-url");
        if (url != null) {
            return url;
        } else {
            return COREBACKENDSERVICE_ENDPOINT;
        }
    }

    public static CoreBackendService getCoreBackendService(String accessToken) {
        CoreBackendServiceImpl coreService = new CoreBackendServiceImpl(createHttpClient(), getCoreBackendServiceEndpoint());
        coreService.setAccessToken(accessToken);
        return coreService;
    }
}
