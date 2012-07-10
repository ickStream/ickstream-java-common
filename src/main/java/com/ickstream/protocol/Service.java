package com.ickstream.protocol;

import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;

public interface Service {
    ServiceInformation getServiceInformation() throws ServiceException, ServiceTimeoutException;
}
