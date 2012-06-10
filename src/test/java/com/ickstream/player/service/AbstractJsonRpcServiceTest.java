/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.service;

import com.ickstream.protocol.JsonRpcRequest;
import com.ickstream.protocol.JsonRpcResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AbstractJsonRpcServiceTest {
    static class Data {
        private String attr1;
        private Integer attr2;
        private Double attr3;

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

        public Double getAttr3() {
            return attr3;
        }

        public void setAttr3(Double attr3) {
            this.attr3 = attr3;
        }
    }

    static class TestService extends AbstractJsonRpcService {
        public Data methodWithoutParams() {
            Data data = new Data();
            data.setAttr1("attr1");
            data.setAttr2(42);
            data.setAttr3(42.3);
            return data;
        }

        public Data methodWithParam(Data input) {
            Data data = new Data();
            data.setAttr1("param" + input.getAttr1());
            data.setAttr2(100 + input.getAttr2());
            data.setAttr3(200.0 + input.getAttr3());
            return data;
        }

        public Data methodWithSimpleParam(@ParamName("attr1") String value) {
            Data data = new Data();
            data.setAttr1("param" + value);
            return data;
        }

        @ResultName("thisresult")
        public Double methodWithSimpleResult(@ParamName("attr2") Double value) {
            return value + 300;
        }

        public Boolean methodWithSimpleBooleanWithoutAnnotation(Boolean value) {
            return !value;
        }

        public String methodWithSimpleStringWithoutAnnotation(String value) {
            return "result" + value;
        }

        @ResultName("result")
        public Data methodWithNamedResultAndParam(@ParamName("param") Data input) {
            Data data = new Data();
            data.setAttr1("namedparam" + input.getAttr1());
            data.setAttr2(1000 + input.getAttr2());
            data.setAttr3(2000.0 + input.getAttr3());
            return data;
        }

        public Boolean methodWithException(Boolean value) {
            throw new PlayerCommandException("Some error occurred");
        }

        public Data methodWithExceptionMissingAttribute(Data input) {
            throw new IllegalArgumentException("attr1 must be specified");
        }
    }

    @Test
    public void testWithoutParams() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithoutParams");
        request.setParams(mapper.readTree("{}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().get("attr1").getTextValue(), "attr1");
        Assert.assertEquals(response.getResult().get("attr2").getIntValue(), 42);
        Assert.assertEquals(response.getResult().get("attr3").getDoubleValue(), 42.3);
    }

    @Test
    public void testWithNamedResultAndParam() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithNamedResultAndParam");
        request.setParams(mapper.readTree("{" +
                "\"param\": {" +
                "\"attr1\":\"value1\"," +
                "\"attr2\": 42," +
                "\"attr3\": 42.3" +
                "}" +
                "}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertNotNull(response.getResult().get("result"));
        Assert.assertEquals(response.getResult().get("result").get("attr1").getTextValue(), "namedparamvalue1");
        Assert.assertEquals(response.getResult().get("result").get("attr2").getIntValue(), 1042);
        Assert.assertEquals(response.getResult().get("result").get("attr3").getDoubleValue(), 2042.3);
    }

    @Test
    public void testWithParams() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithParam");
        request.setParams(mapper.readTree("{" +
                "\"attr1\":\"value1\"," +
                "\"attr2\": 42," +
                "\"attr3\": 42.3" +
                "}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().get("attr1").getTextValue(), "paramvalue1");
        Assert.assertEquals(response.getResult().get("attr2").getIntValue(), 142);
        Assert.assertEquals(response.getResult().get("attr3").getDoubleValue(), 242.3);
    }

    @Test
    public void testWithSimpleParam() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithSimpleParam");
        request.setParams(mapper.readTree("{" +
                "\"attr1\":\"myvalue\"" +
                "}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().get("attr1").getTextValue(), "parammyvalue");
        Assert.assertFalse(response.getResult().has("attr2"));
        Assert.assertFalse(response.getResult().has("attr3"));
    }

    @Test
    public void testWithSimpleResult() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithSimpleResult");
        request.setParams(mapper.readTree("{" +
                "\"attr2\":42.3" +
                "}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().get("thisresult").getDoubleValue(), 342.3);
    }

    @Test
    public void testWithSimpleBooleanWithoutAnnotation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithSimpleBooleanWithoutAnnotation");
        request.setParams(mapper.readValue("true", JsonNode.class));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getBooleanValue(), false);
    }

    @Test
    public void testWithSimpleStringWithoutAnnotation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithSimpleStringWithoutAnnotation");
        request.setParams(mapper.readValue("\"thisvalue\"", JsonNode.class));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNotNull(response.getResult());
        Assert.assertEquals(response.getResult().getTextValue(), "resultthisvalue");
    }

    @Test
    public void testWithInvalidParamType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithSimpleBooleanWithoutAnnotation");
        request.setParams(mapper.readValue("\"thisvalue\"", JsonNode.class));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNull(response.getResult());
        Assert.assertNotNull(response.getError());
        Assert.assertEquals(response.getError().get("code").getIntValue(), -32602);
        Assert.assertNotNull(response.getError().get("message").getTextValue());
    }

    @Test
    public void testWithInvalidParamTypeExtraAttribute() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithParam");
        request.setParams(mapper.readTree("{" +
                "\"attr1\":\"value1\"," +
                "\"attr2\": 42," +
                "\"attr3\": 42.3," +
                "\"extraattr\": \"somevalue\"" +
                "}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNull(response.getResult());
        Assert.assertNotNull(response.getError());
        Assert.assertEquals(response.getError().get("code").getIntValue(), -32602);
        Assert.assertNotNull(response.getError().get("message").getTextValue());
    }

    @Test
    public void testWithMissingAttribute() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithExceptionMissingAttribute");
        request.setParams(mapper.readTree("{" +
                "\"attr2\": 42," +
                "\"attr3\": 42.3" +
                "}"));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNull(response.getResult());
        Assert.assertNotNull(response.getError());
        Assert.assertEquals(response.getError().get("code").getIntValue(), -32602);
        Assert.assertNotNull(response.getError().get("message").getTextValue());
    }

    @Test
    public void testWithException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithException");
        request.setParams(mapper.readValue("true", JsonNode.class));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNull(response.getResult());
        Assert.assertNotNull(response.getError());
        Assert.assertEquals(response.getError().get("code").getIntValue(), -32602);
        Assert.assertEquals(response.getError().get("message").getTextValue(), "Some error occurred");
    }

    @Test
    public void testWithUnknownMethod() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonRpcRequest request = new JsonRpcRequest();
        request.setId(1);
        request.setMethod("methodWithUnknownMethod");
        request.setParams(mapper.readValue("true", JsonNode.class));

        TestService service = new TestService();
        JsonRpcResponse response = service.invoke(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertNull(response.getResult());
        Assert.assertNotNull(response.getError());
        Assert.assertEquals(response.getError().get("code").getIntValue(), -32601);
        Assert.assertTrue(response.getError().get("message").getTextValue().contains("methodWithUnknownMethod"));
    }
}
