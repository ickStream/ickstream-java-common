/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.common.jsonrpc.JsonRpcError;
import com.ickstream.common.jsonrpc.JsonRpcErrors;
import com.ickstream.protocol.service.AccountInformation;

/**
 * Interface which should be implemented by all services which are tied to a backend service using a user account
 */
public interface PersonalizedService extends CloudService {
    /**
     * Get account information about the account the current user is tied to in the backend service
     *
     * @return Account information
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public AccountInformation getAccountInformation();
}
