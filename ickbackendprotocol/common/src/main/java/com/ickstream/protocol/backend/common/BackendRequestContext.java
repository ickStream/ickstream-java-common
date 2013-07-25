package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.corebackend.*;

/**
 * A request context for calls to {@link CoreBackendService}, this is primarily a way for {@link CoreBackendServiceImpl}
 * to reuse information from previous calls from the same application.
 * <p/>
 * A service should NOT rely on that this context exists as it's really an implementation optimization to improve
 * performance
 */
public class BackendRequestContext {
    private DeviceResponse device;
    private UserResponse user;
    private ApplicationResponse application;
    private AuthenticationProviderResponse authenticationProvider;
    private ServiceResponse service;

    public DeviceResponse getDevice() {
        return device;
    }

    public void setDevice(DeviceResponse device) {
        this.device = device;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public ApplicationResponse getApplication() {
        return application;
    }

    public void setApplication(ApplicationResponse application) {
        this.application = application;
    }

    public AuthenticationProviderResponse getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AuthenticationProviderResponse authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public ServiceResponse getService() {
        return service;
    }

    public void setService(ServiceResponse service) {
        this.service = service;
    }
}
