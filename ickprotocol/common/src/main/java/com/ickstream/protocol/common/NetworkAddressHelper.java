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

package com.ickstream.protocol.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Utility class which provides methods to lookup information about the local network hardware and configuration
 */
public class NetworkAddressHelper {
    /**
     * Get the IP address of the local network card
     *
     * @return The IP address or "127.0.0.1" if no IP address could be detected
     */
    public static String getNetworkAddress() {
        InetAddress currentInetAddress = getInetAddress();
        if (currentInetAddress != null) {
            return currentInetAddress.getHostAddress();
        } else {
            return "127.0.0.1";
        }
    }

    private static InetAddress getInetAddress() {
        InetAddress currentAddress = null;
        try {
            InetAddress addrs[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());


            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                    currentAddress = addr;
                    break;
                }
            }
            if (currentAddress == null || currentAddress.getHostAddress() == null || currentAddress.getHostAddress().length() == 0) {
                try {
                    Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
                    for (NetworkInterface iface : Collections.list(ifaces)) {
                        Enumeration<InetAddress> raddrs = iface.getInetAddresses();
                        for (InetAddress raddr : Collections.list(raddrs)) {
                            if (!raddr.isLoopbackAddress() && raddr.isSiteLocalAddress()) {
                                currentAddress = raddr;
                                break;
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
        }
        if (currentAddress != null && currentAddress.getHostAddress() != null && currentAddress.getHostAddress().length() > 0) {
            return currentAddress;
        }
        return null;
    }

    /**
     * Get the hardware address of the local network card
     *
     * @return The hardware address of the local network card or null if no hardware address could be retrieved
     */
    public static String getNetworkHardwareAddress() {
        InetAddress currentInetAddress = getInetAddress();
        if (currentInetAddress != null) {
            try {
                byte[] hardwareAddress = NetworkInterface.getByInetAddress(currentInetAddress).getHardwareAddress();
                if (hardwareAddress != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < hardwareAddress.length; i++) {
                        sb.append(String.format("%02X%s", hardwareAddress[i], (i < hardwareAddress.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
