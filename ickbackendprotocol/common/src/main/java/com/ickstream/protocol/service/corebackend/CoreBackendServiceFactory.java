package com.ickstream.protocol.service.corebackend;

import com.ickstream.protocol.common.ServiceFactory;

import java.util.HashMap;
import java.util.Map;

public class CoreBackendServiceFactory extends ServiceFactory {
    private static final String COREBACKENDSERVICE_ENDPOINT = "http://api.ickstream.com/ickstream-cloud-core/backend/jsonrpc";

    private static final Map<String, CoreBackendServiceImpl> CORE_BACKEND_SERVICE_CACHE = new HashMap<String, CoreBackendServiceImpl>();

    private static String getCoreBackendServiceEndpoint() {
        String url = System.getProperty("ickstream-core-backend-url");
        if (url != null) {
            return url;
        } else {
            return COREBACKENDSERVICE_ENDPOINT;
        }
    }

    public static synchronized CoreBackendService getCoreBackendService(String accessToken) {
        CoreBackendServiceImpl coreService = CORE_BACKEND_SERVICE_CACHE.get(accessToken);
        if (coreService == null) {
            coreService = new CoreBackendServiceImpl(createHttpClient(), getCoreBackendServiceEndpoint());
            coreService.setAccessToken(accessToken);
            CORE_BACKEND_SERVICE_CACHE.put(accessToken, coreService);
        }
        return coreService;
    }
}
