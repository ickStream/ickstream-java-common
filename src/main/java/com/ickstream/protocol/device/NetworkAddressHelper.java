/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkAddressHelper {
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
