/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import junit.framework.Assert;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;

public class AsyncJsonRpcClientTest extends AbstractJsonRpcTest {
    ObjectMapper mapper = new ObjectMapper();

    private static class MessageSenderImpl implements MessageSender {
        private String message;

        @Override
        public void sendMessage(String message) {
            this.message = message;
        }
    }

    private static class TestData {
        private String attr1;
        private Integer attr2;
        private Boolean attr3;

        private TestData() {
        }

        private TestData(String attr1, Integer attr2, Boolean attr3) {
            this.attr1 = attr1;
            this.attr2 = attr2;
            this.attr3 = attr3;
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public Integer getAttr2() {
            return attr2;
        }

        public void setAttr2(Integer attr2) {
            this.attr2 = attr2;
        }

        public Boolean getAttr3() {
            return attr3;
        }

        public void setAttr3(Boolean attr3) {
            this.attr3 = attr3;
        }
    }

    @Test
    public void testRequestResponse() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false, false};
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[0] = true;
            }

            @Override
            public void onFinished() {
                validated[1] = true;
            }
        });

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        JsonRpcResponse response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);
    }

    @Test
    public void testRequestResponseWithoutTimeoutTriggered() throws IOException, InterruptedException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false, false, false};
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[0] = true;

            }

            @Override
            public void onTimeout() {
                validated[1] = true;
            }

            @Override
            public void onFinished() {
                validated[2] = true;
            }
        }, 5000);

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        Thread.sleep(1000);

        JsonRpcResponse response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);
        Assert.assertFalse(validated[1]);
        Assert.assertTrue(validated[2]);
    }

    @Test
    public void testRequestResponseWithTimeoutTriggered() throws IOException, InterruptedException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false, false, false};
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[0] = true;

            }

            @Override
            public void onTimeout() {
                validated[1] = true;
            }

            @Override
            public void onFinished() {
                validated[2] = true;
            }
        }, 100);

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        Thread.sleep(1000);

        JsonRpcResponse response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertFalse(validated[0]);
        Assert.assertTrue(validated[1]);
        Assert.assertTrue(validated[2]);
    }

    @Test
    public void testRequestFullResponseData() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false};
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true), JsonRpcResponse.class, new MessageHandlerAdapter<JsonRpcResponse>() {
            @Override
            public void onMessage(JsonRpcResponse message) {
                Assert.assertNotNull(message.getJsonrpc());
                Assert.assertNotNull(message.getId());
                Assert.assertNull(message.getError());
                Assert.assertEquals("value3", message.getResult().get("attr1").getTextValue());
                Assert.assertEquals(4, message.getResult().get("attr2").getIntValue());
                Assert.assertEquals(false, message.getResult().get("attr3").getBooleanValue());
                validated[0] = true;

            }
        });

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        JsonRpcResponse response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);
    }

    @Test
    public void testMultipleRequestResponse() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false, false};
        String id1 = client.sendRequest("someMethod", new TestData("value1", 2, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[0] = true;

            }
        });

        Assert.assertEquals(id1, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("2", getParamFromJson(sender.message, "params.attr2"));

        String id2 = client.sendRequest("someMethod", new TestData("value1", 4, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value5", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(6), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[1] = true;

            }
        });

        Assert.assertEquals(id2, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("4", getParamFromJson(sender.message, "params.attr2"));

        JsonRpcResponse response = new JsonRpcResponse("2.0", id2);
        response.setResult(mapper.valueToTree(new TestData("value5", 6, false)));
        client.onResponse(response);
        Assert.assertFalse(validated[0]);
        Assert.assertTrue(validated[1]);

        response = new JsonRpcResponse("2.0", id1);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);

    }

    @Test
    public void testRequestWithExtraResponseWithUnusedId() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false};
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[0] = true;

            }
        });

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        JsonRpcResponse response = new JsonRpcResponse("2.0", id + "1");
        response.setResult(mapper.valueToTree(new TestData("value5", 6, false)));
        client.onResponse(response);
        Assert.assertFalse(validated[0]);

        response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);
    }

    @Test
    public void testRequestWithDuplicateResponse() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        final boolean[] validated = {false};
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true), TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[0] = true;

            }
        });

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        JsonRpcResponse response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);

        response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value5", 6, false)));
        client.onResponse(response);
        Assert.assertTrue(validated[0]);
    }

    @Test
    public void testRequestWithoutAnyResponse() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);
        String id = client.sendRequest("someMethod", new TestData("value1", 2, true));

        Assert.assertEquals(id, getParamFromJson(sender.message, "id"));
        Assert.assertEquals("value1", getParamFromJson(sender.message, "params.attr1"));

        JsonRpcResponse response = new JsonRpcResponse("2.0", id);
        response.setResult(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onResponse(response);
    }

    @Test
    public void testNotification() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(2), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[0] = true;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 2, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
    }

    @Test
    public void testNotificationFullRequestData() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false};
        client.addNotificationListener("someNotification", JsonRpcRequest.class, new MessageHandlerAdapter<JsonRpcRequest>() {
            @Override
            public void onMessage(JsonRpcRequest message) {
                Assert.assertNull(message.getId());
                Assert.assertEquals("someNotification", message.getMethod());
                Assert.assertEquals("value1", message.getParams().get("attr1").getTextValue());
                Assert.assertEquals(2, message.getParams().get("attr2").getIntValue());
                Assert.assertEquals(true, message.getParams().get("attr3").getBooleanValue());
                validated[0] = true;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 2, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
    }

    @Test
    public void testNotificationWithoutListener() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {true};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(2), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[0] = false;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someOtherNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 2, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
    }

    @Test
    public void testDuplicateNotifications() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false, false};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                if (message.attr2 == 1) {
                    Assert.assertEquals("value1", message.getAttr1());
                    Assert.assertEquals(Integer.valueOf(1), message.getAttr2());
                    Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                    validated[0] = true;
                } else if (message.attr2 == 2) {
                    Assert.assertEquals("value2", message.getAttr1());
                    Assert.assertEquals(Integer.valueOf(2), message.getAttr2());
                    Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                    validated[1] = true;
                } else {
                    Assert.assertTrue(false);
                }
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 1, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);

        request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value2", 2, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);
    }

    @Test
    public void testNotificationsMultipleListenersSameType() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false, false};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(1), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[0] = true;
            }
        });
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(1), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[1] = true;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 1, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);
    }

    @Test
    public void testNotificationsMultipleListenersDifferentType() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false, false};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(1), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[0] = true;
            }
        });
        client.addNotificationListener("someNotification", JsonNode.class, new MessageHandlerAdapter<JsonNode>() {
            @Override
            public void onMessage(JsonNode message) {
                Assert.assertEquals("value1", message.get("attr1").getTextValue());
                Assert.assertEquals(1, message.get("attr2").getIntValue());
                Assert.assertEquals(true, message.get("attr3").getBooleanValue());
                validated[1] = true;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 1, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);
    }

    @Test
    public void testNotificationsMultipleListenersDifferentNotificationSameType() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false, false};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(1), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[0] = true;
            }
        });
        client.addNotificationListener("someOtherNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value3", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(4), message.getAttr2());
                Assert.assertEquals(Boolean.FALSE, message.getAttr3());
                validated[1] = true;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 1, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertFalse(validated[1]);

        request = new JsonRpcRequest("2.0", null);
        request.setMethod("someOtherNotification");
        request.setParams(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);
    }

    @Test
    public void testNotificationsMultipleListenersDifferentNotificationDifferentType() throws IOException {
        MessageSenderImpl sender = new MessageSenderImpl();
        AsyncJsonRpcClient client = new AsyncJsonRpcClient(sender);

        final boolean[] validated = {false, false};
        client.addNotificationListener("someNotification", TestData.class, new MessageHandlerAdapter<TestData>() {
            @Override
            public void onMessage(TestData message) {
                Assert.assertEquals("value1", message.getAttr1());
                Assert.assertEquals(Integer.valueOf(1), message.getAttr2());
                Assert.assertEquals(Boolean.TRUE, message.getAttr3());
                validated[0] = true;
            }
        });
        client.addNotificationListener("someOtherNotification", JsonNode.class, new MessageHandlerAdapter<JsonNode>() {
            @Override
            public void onMessage(JsonNode message) {
                Assert.assertEquals("value3", message.get("attr1").getTextValue());
                Assert.assertEquals(4, message.get("attr2").getIntValue());
                Assert.assertEquals(false, message.get("attr3").getBooleanValue());
                validated[1] = true;
            }
        });

        JsonRpcRequest request = new JsonRpcRequest("2.0", null);
        request.setMethod("someNotification");
        request.setParams(mapper.valueToTree(new TestData("value1", 1, true)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertFalse(validated[1]);

        request = new JsonRpcRequest("2.0", null);
        request.setMethod("someOtherNotification");
        request.setParams(mapper.valueToTree(new TestData("value3", 4, false)));
        client.onRequest(request);

        Assert.assertTrue(validated[0]);
        Assert.assertTrue(validated[1]);
    }
}
