package com.ickstream.protocol;

import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;

public interface Service {
    ServiceInformation getServiceInformation() throws ServiceException, ServiceTimeoutException;

    ServiceInformation getServiceInformation(Integer timeout) throws ServiceException, ServiceTimeoutException;

    void getServiceInformation(MessageHandler<ServiceInformation> messageHandler);

    void getServiceInformation(MessageHandler<ServiceInformation> messageHandler, Integer timeout);
}
