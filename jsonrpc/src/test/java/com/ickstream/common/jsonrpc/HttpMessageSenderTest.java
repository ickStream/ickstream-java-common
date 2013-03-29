/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;

public class HttpMessageSenderTest {
    private static final String ENDPOINT = "http://example.org/jsonrpc";

    private HttpClient createClient(final String endpoint, final String result, final int statusCode, final String statusPhrase) {
        try {
            HttpClient client = Mockito.mock(HttpClient.class);
            HttpResponse response = Mockito.mock(HttpResponse.class);
            if (result != null) {
                HttpEntity entity = new StringEntity(result);
                Mockito.when(response.getEntity()).thenReturn(entity);
            }
            StatusLine statusLine = Mockito.mock(StatusLine.class);
            Mockito.when(statusLine.getStatusCode()).thenReturn(statusCode);
            Mockito.when(statusLine.getReasonPhrase()).thenReturn(statusPhrase);
            Mockito.when(response.getStatusLine()).thenReturn(statusLine);
            Mockito.when(client.execute(Mockito.any(HttpPost.class))).thenReturn(response);
            return client;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpClient createClientFails(final String endpoint, Throwable t) {
        try {
            HttpClient client = Mockito.mock(HttpClient.class);
            Mockito.when(client.execute(Mockito.any(HttpPost.class))).thenThrow(t);
            return client;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInvalidJson() {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":\"\n" +
                "}";

        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, null, 200, null), ENDPOINT, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getResult());
                Assert.assertNotNull(response.getError());
                Assert.assertEquals(response.getError().getCode(), JsonRpcError.INVALID_JSON);
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testSuccess() {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        String jsonResponse = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"result\":\"42\"\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, jsonResponse, 200, jsonResponse), ENDPOINT, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getError());
                Assert.assertNotNull(response.getResult());
                Assert.assertEquals("42", response.getResult().asText());
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testSuccessAsynchronous() throws InterruptedException {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        String jsonResponse = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"result\":\"42\"\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, jsonResponse, 200, jsonResponse), ENDPOINT, true, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getError());
                Assert.assertNotNull(response.getResult());
                Assert.assertEquals("42", response.getResult().asText());
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        int i = 0;
        while (!executed[0] && i < 10) {
            Thread.sleep(100);
            i++;
        }
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testAuthorization() {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, null, 401, "Unauthorized"), ENDPOINT, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getResult());
                Assert.assertNotNull(response.getError());
                Assert.assertEquals(response.getError().getCode(), JsonRpcError.UNAUTHORIZED);
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testAuthorizationAsynchronous() throws InterruptedException {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, null, 401, "Unauthorized"), ENDPOINT, true, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getResult());
                Assert.assertNotNull(response.getError());
                Assert.assertEquals(response.getError().getCode(), JsonRpcError.UNAUTHORIZED);
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        int i = 0;
        while (!executed[0] && i < 10) {
            Thread.sleep(100);
            i++;
        }
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testServiceProblem404() {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, null, 404, "Not found"), ENDPOINT, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getResult());
                Assert.assertNotNull(response.getError());
                Assert.assertEquals(response.getError().getCode(), JsonRpcError.SERVICE_ERROR);
                Assert.assertEquals(response.getError().getMessage(), "Not found");
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testServiceProblem500() {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClient(ENDPOINT, null, 500, "Internal server error"), ENDPOINT, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getResult());
                Assert.assertNotNull(response.getError());
                Assert.assertEquals(response.getError().getCode(), JsonRpcError.SERVICE_ERROR);
                Assert.assertEquals(response.getError().getMessage(), "Internal server error");
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        Assert.assertTrue(executed[0]);
    }

    @Test
    public void testServiceProblemExecuteFails() {
        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"someMethod\",\n" +
                "\t\"params\":{}\n" +
                "}";
        final boolean[] executed = {false};
        new HttpMessageSender(createClientFails(ENDPOINT, new ClientProtocolException("Some error")), ENDPOINT, null, new JsonRpcResponseHandler() {
            @Override
            public boolean onResponse(JsonRpcResponse response) {
                Assert.assertNull(response.getResult());
                Assert.assertNotNull(response.getError());
                Assert.assertEquals(response.getError().getCode(), JsonRpcError.SERVICE_ERROR);
                Assert.assertEquals(response.getError().getMessage(), "Some error");
                executed[0] = true;
                return true;
            }
        }).sendMessage(jsonRequest);
        Assert.assertTrue(executed[0]);
    }
}
