package com.ickstream.protocol;

import com.ickstream.protocol.cloud.ServerException;

public interface Service {
    ServiceInformation getServiceInformation() throws ServerException;
}
