/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.common.jsonrpc;

/**
 * Interface for a message handler able to receive message responses or notifications. If you want to ensure
 * compatibility with future versions you should extend from {@link MessageHandlerAdapter} instead of implementing
 * this interface directly.
 *
 * @param <T> The type of data which the message handler will process
 */
public interface MessageHandler<T> {
    /**
     * Called when the response or notification is received and it's not an error
     *
     * @param message The actual message data
     */
    void onMessage(T message);

    /**
     * Called when an error of a request is received
     *
     * @param code    The JSON-RPC error code
     * @param message The JSON-RPC error message
     * @param data    The JSON-RPC error data
     */
    void onError(int code, String message, String data);

    /**
     * Called when a timeout occurs, if this method is called the {@link #onMessage(Object)} method will never be
     * called if the response is received after the onTimeout call has happened.
     */
    void onTimeout();

    /**
     * Called for each response or notification after either {@link #onMessage(Object)} or {@link #onTimeout()} or
     * {@link #onError(int, String, String)} has been processed completely. The typically usage of this method is to
     * perform clean-up needed after the processing is finished.
     */
    void onFinished();
}
