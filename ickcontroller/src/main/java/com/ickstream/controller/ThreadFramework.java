/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller;

public interface ThreadFramework {
    /**
     * Invoke a Runnable either in the current thread or as a new thread
     * or in an existing thread. The typical usage is to implement a ThreadFramework
     * that calls SwingUtilities.invokeLater or on Android Activity.runOnUiThread.
     * The purpose is to make sure all callbacks are called by the UI thread to avoid
     * having to make all client code thread safe.
     *
     * @param runnable
     */
    void invoke(Runnable runnable);
}
