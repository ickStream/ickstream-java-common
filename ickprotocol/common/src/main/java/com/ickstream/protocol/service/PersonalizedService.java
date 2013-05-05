/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service;

import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;

public interface PersonalizedService extends Service {
    AccountInformation getAccountInformation() throws ServiceException, ServiceTimeoutException;

    AccountInformation getAccountInformation(Integer timeout) throws ServiceException, ServiceTimeoutException;

    void getAccountInformation(MessageHandler<AccountInformation> messageHandler);

    void getAccountInformation(MessageHandler<AccountInformation> messageHandler, Integer timeout);
}
