/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.common.jsonrpc.JsonHelper;
import com.ickstream.protocol.common.ServiceFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Geographical area detector helper class that detects geographical area based on an
 * IP address
 */
public class GeoIpHelper {
    /**
     * Detect country based on the specified IP address
     *
     * @param ipAddress An IP address
     * @return The country code or null if the country couldn't be detected
     */
    public static String detectCountry(String ipAddress) {
        HttpGet req = new HttpGet("http://freegeoip.net/json/" + ipAddress);
        try {
            HttpResponse resp = ServiceFactory.createHttpClient().execute(req);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseString = EntityUtils.toString(resp.getEntity());
                JsonNode json = new JsonHelper().stringToObject(responseString, JsonNode.class);
                if (json.has("country_code")) {
                    return json.get("country_code").asText();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
