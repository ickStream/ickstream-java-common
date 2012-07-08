package com.ickstream.common.jsonrpc;

public interface MessageLogger {
    void onOutgoingMessage(String destination, String message);

    void onIncomingMessage(String source, String message);
}
