/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class StreamJsonRpcServiceTest extends AbstractJsonRpcTest {
    public static class SomeException extends Exception {
    }

    public static class SomeOtherException extends Exception {
    }

    public static class ExtraParameters {
        private String param2;
        private Boolean param3;
        private Integer param4;

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }

        public Boolean getParam3() {
            return param3;
        }

        public void setParam3(Boolean param3) {
            this.param3 = param3;
        }

        public Integer getParam4() {
            return param4;
        }

        public void setParam4(Integer param4) {
            this.param4 = param4;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (param2 != null) {
                sb.append("param2");
            }
            if (param3 != null) {
                sb.append("param3");
            }
            if (param4 != null) {
                sb.append("param4");
            }
            return sb.toString();
        }
    }

    public static interface SimpleTypeMethods {
        String testMethod(@JsonRpcParam(name = "param1") String param1);

        String testMethod(@JsonRpcParam(name = "param1") Boolean param1);

        String testMethod(@JsonRpcParam(name = "param1") Integer param1);

        String testMethod(@JsonRpcParam(name = "param1") ExtraParameters param1);

        String testMethod(@JsonRpcParam(name = "param1") List<String> param1);

        String testMethodOnlyString(@JsonRpcParam(name = "param1") String param1);

        String testMethodOnlyBoolean(@JsonRpcParam(name = "param1") Boolean param1);

        String testMethodOnlyInteger(@JsonRpcParam(name = "param1") Integer param1);
    }

    public static interface SimpleParameterMethods {
        String testMethod();

        String testMethodOther(@JsonRpcParam(name = "other") String param1);

        String testMethod(@JsonRpcParam(name = "param1") String param1);

        String testMethod(@JsonRpcParam(name = "param1", optional = true) String param1, @JsonRpcParam(name = "param2") String param2);
    }

    public static interface ParameterStructureMethods {
        String testMethodStrict(@JsonRpcParamStructure ExtraParameters extraParameters);

        String testMethodStrict(@JsonRpcParam(name = "param1") String param1, @JsonRpcParamStructure ExtraParameters extraParameters);

        String testMethodJson(@JsonRpcParamStructure JsonNode extraParameters);

        String testMethodJson(@JsonRpcParam(name = "param1") String param1, @JsonRpcParamStructure JsonNode extraParameters);

        String testMethodMap(@JsonRpcParamStructure Map<String, JsonNode> extraParameters);

        String testMethodMap(@JsonRpcParam(name = "param1") String param1, @JsonRpcParamStructure Map<String, JsonNode> extraParameters);
    }

    public static interface ExceptionMethods {

        @JsonRpcErrors({
                @JsonRpcError(exception = SomeException.class, code = -32050, message = "Some error"),
                @JsonRpcError(exception = SomeOtherException.class, code = -32051, message = "Some other error")
        })
        String testMethod(@JsonRpcParam(name = "param1", optional = true) String param1) throws SomeException, SomeOtherException;
    }

    public static class ComplexResult {
        private Integer resultInteger;
        private String resultString;
        private Boolean resultBoolean;
        private JsonNode resultJson;

        public Integer getResultInteger() {
            return resultInteger;
        }

        public void setResultInteger(Integer resultInteger) {
            this.resultInteger = resultInteger;
        }

        public String getResultString() {
            return resultString;
        }

        public void setResultString(String resultString) {
            this.resultString = resultString;
        }

        public Boolean getResultBoolean() {
            return resultBoolean;
        }

        public void setResultBoolean(Boolean resultBoolean) {
            this.resultBoolean = resultBoolean;
        }

        public JsonNode getResultJson() {
            return resultJson;
        }

        public void setResultJson(JsonNode resultJson) {
            this.resultJson = resultJson;
        }
    }

    public static interface ComplexResultMethods {
        void voidMethod();

        @JsonRpcResult("result1")
        String namedResultMethod();

        @JsonRpcResult("result1")
        ComplexResult namedResultFilledMethod();

        ComplexResult nullMethod();

        ComplexResult emptyMethod();

        ComplexResult filledMethod();

        Map<String, String> stringMapMethod();

        Map<String, JsonNode> jsonMapMethod() throws IOException;

        List<String> stringArrayMethod();

        List<JsonNode> jsonArrayMethod() throws IOException;
    }

    public static class SimpleTypeMethodsImpl implements SimpleTypeMethods {
        @Override
        public String testMethod(@JsonRpcParam(name = "param1") String param1) {
            return "testMethodParam1String";
        }

        @Override
        public String testMethod(@JsonRpcParam(name = "param1") Boolean param1) {
            return "testMethodParam1Boolean";
        }

        @Override
        public String testMethod(@JsonRpcParam(name = "param1") Integer param1) {
            return "testMethodParam1Number";
        }

        @Override
        public String testMethod(@JsonRpcParam(name = "param1") ExtraParameters param1) {
            return "testMethodParam1Object";
        }

        @Override
        public String testMethod(@JsonRpcParam(name = "param1") List<String> param1) {
            return "testMethodParam1List";
        }

        @Override
        public String testMethodOnlyString(@JsonRpcParam(name = "param1") String param1) {
            return "testMethodParam1OnlyString";
        }

        @Override
        public String testMethodOnlyBoolean(@JsonRpcParam(name = "param1") Boolean param1) {
            return "testMethodParam1OnlyBoolean";
        }

        @Override
        public String testMethodOnlyInteger(@JsonRpcParam(name = "param1") Integer param1) {
            return "testMethodParam1OnlyInteger";
        }
    }

    public static class SimpleParameterMethodsImpl implements SimpleParameterMethods {
        public String testMethodUnknown() {
            // This should not be exposed as it's not part of the interface
            return "testMethodUnkown";
        }

        @Override
        public String testMethod() {
            return "testMethod";
        }

        @Override
        public String testMethodOther(@JsonRpcParam(name = "other") String param1) {
            return "testMethodOther";
        }

        @Override
        public String testMethod(@JsonRpcParam(name = "param1") String param1) {
            return "testMethodParam1";
        }

        @Override
        public String testMethod(@JsonRpcParam(name = "param1", optional = true) String param1, @JsonRpcParam(name = "param2") String param2) {
            return "testMethodParam1Param2";
        }
    }

    public static class ParameterStructureMethodsImpl implements ParameterStructureMethods {
        @Override
        public String testMethodStrict(@JsonRpcParamStructure ExtraParameters extraParameters) {
            Assert.assertNotNull(extraParameters);
            return "testMethodStrictWithExtras" + (extraParameters != null && extraParameters.toString().length() > 0 ? "_" + extraParameters.toString() : "");
        }

        @Override
        public String testMethodStrict(@JsonRpcParam(name = "param1") String param1, @JsonRpcParamStructure ExtraParameters extraParameters) {
            Assert.assertNotNull(extraParameters);
            return "testMethodStrictParam1WithExtras" + (extraParameters != null && extraParameters.toString().length() > 0 ? "_" + extraParameters.toString() : "");
        }

        @Override
        public String testMethodJson(@JsonRpcParamStructure JsonNode extraParameters) {
            Assert.assertNotNull(extraParameters);
            String extraAttributes = "";
            if (extraParameters != null) {
                Iterator<Map.Entry<String, JsonNode>> fields = extraParameters.getFields();
                List<String> fieldNames = new ArrayList<String>();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> next = fields.next();
                    fieldNames.add(next.getKey());
                }
                if (fieldNames.size() > 0) {
                    extraAttributes = "_" + StringUtils.join(fieldNames.toArray());
                }
            }

            return "testMethodJsonWithExtras" + extraAttributes;
        }

        @Override
        public String testMethodJson(@JsonRpcParam(name = "param1") String param1, @JsonRpcParamStructure JsonNode extraParameters) {
            Assert.assertNotNull(extraParameters);
            String extraAttributes = "";
            if (extraParameters != null) {
                Iterator<Map.Entry<String, JsonNode>> fields = extraParameters.getFields();
                List<String> fieldNames = new ArrayList<String>();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> next = fields.next();
                    fieldNames.add(next.getKey());
                }
                if (fieldNames.size() > 0) {
                    extraAttributes = "_" + StringUtils.join(fieldNames.toArray());
                }
            }

            return "testMethodJsonParam1WithExtras" + extraAttributes;
        }

        @Override
        public String testMethodMap(@JsonRpcParamStructure Map<String, JsonNode> extraParameters) {
            Assert.assertNotNull(extraParameters);
            return "testMethodMapWithExtras" + (extraParameters != null && extraParameters.size() > 0 ? "_" + StringUtils.join(extraParameters.keySet().toArray()) : "");
        }

        @Override
        public String testMethodMap(@JsonRpcParam(name = "param1") String param1, @JsonRpcParamStructure Map<String, JsonNode> extraParameters) {
            Assert.assertNotNull(extraParameters);
            return "testMethodMapParam1WithExtras" + (extraParameters != null && extraParameters.size() > 0 ? "_" + StringUtils.join(extraParameters.keySet().toArray()) : "");
        }
    }

    public static class ExceptionMethodsImpl implements ExceptionMethods {
        @Override
        public String testMethod(@JsonRpcParam(name = "param1") String param1) throws SomeException, SomeOtherException {
            if (param1 == null) {
                throw new IllegalArgumentException("param1");
            } else if (param1.equals("some")) {
                throw new SomeException();
            } else if (param1.equals("someOther")) {
                throw new SomeOtherException();
            }
            return "testMethod";
        }
    }

    public static class ComplexResultMethodsImpl implements ComplexResultMethods {
        @Override
        public String namedResultMethod() {
            return "namedResultMethod";
        }

        @Override
        public ComplexResult namedResultFilledMethod() {
            return filledMethod();
        }

        @Override
        public void voidMethod() {
        }

        @Override
        public ComplexResult nullMethod() {
            return null;
        }

        @Override
        public ComplexResult emptyMethod() {
            return new ComplexResult();
        }

        @Override
        public ComplexResult filledMethod() {
            ComplexResult result = new ComplexResult();
            result.setResultInteger(1);
            result.setResultString("2");
            result.setResultBoolean(true);
            ObjectNode object = new ObjectMapper().createObjectNode();
            object.put("attr1", 3);
            object.put("attr2", "4");
            result.setResultJson(object);
            return result;
        }

        @Override
        public Map<String, String> stringMapMethod() {
            Map<String, String> result = new TreeMap<String, String>();
            result.put("attr1", "value1");
            result.put("attr2", "value2");
            return result;
        }

        @Override
        public Map<String, JsonNode> jsonMapMethod() throws IOException {
            Map<String, JsonNode> result = new TreeMap<String, JsonNode>();
            result.put("attr1", new ObjectMapper().readTree("{\"attr3\":1}"));
            result.put("attr2", new ObjectMapper().readTree("{\"attr4\":2}"));
            return result;
        }

        @Override
        public List<String> stringArrayMethod() {
            return Arrays.asList("one", "two", "three");
        }

        @Override
        public List<JsonNode> jsonArrayMethod() throws IOException {
            return Arrays.asList(new ObjectMapper().readTree("{\"attr1\":\"one\"}"), new ObjectMapper().readTree("{\"attr2\":\"two\"}"), new ObjectMapper().readTree("{\"attr3\":\"three\"}"));
        }
    }

    @Test
    public void testInvalidJson() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        String jsonRequest = "" +
                "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":1,\n" +
                "\t\"method\":\"testMethod\",\n" +
                "\t\"params\":\"\n" +
                "}";

        service.handle(IOUtils.toInputStream(jsonRequest), new WriterOutputStream(outputString));

        Assert.assertEquals("-32700", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithoutMethod() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", null, null)), new WriterOutputStream(outputString));


        Assert.assertEquals("-32600", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithoutVersion() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("", "1", "testMethod", null)), new WriterOutputStream(outputString));


        Assert.assertEquals("-32600", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithoutParameters() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", null)), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethod", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithoutParametersUnknownMethod() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodUnknown", null)), new WriterOutputStream(outputString));


        Assert.assertEquals("-32601", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithEmptyParameters() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethod", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithSingleParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithSingleParameterWrongParameters() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOther", "{\"param1\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("-32602", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithSingleParameterTooFewParameters() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOther", "{}")), new WriterOutputStream(outputString));


        Assert.assertEquals("-32602", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithSingleParameterWithoutOptional() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1Param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithTwoParametersWithOptional() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleParameterMethodsImpl(), SimpleParameterMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":\"something\",\"param2\":\"something\"}")), new WriterOutputStream(outputString));

        Assert.assertEquals("testMethodParam1Param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithObjectParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":{\"param2\":\"something\"}}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1Object", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithArrayParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":[\"something\"]}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1List", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStringParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":\"1\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1String", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStringParameterOnlyMethod() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOnlyString", "{\"param1\":\"1\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1OnlyString", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStringParameterOnlyMethodNotExisting() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOnlyBoolean", "{\"param1\":\"1\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("-32602", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithNumberParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":1}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1Number", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithNumberParameterOnlyMethod() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOnlyInteger", "{\"param1\":1}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1OnlyInteger", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithNumberParameterOnlyMethodNotExisting() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOnlyString", "{\"param1\":1}")), new WriterOutputStream(outputString));


        Assert.assertEquals("-32602", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithBooleanParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":true}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1Boolean", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithBooleanParameterOnlyMethod() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOnlyBoolean", "{\"param1\":true}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodParam1OnlyBoolean", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithBooleanParameterOnlyMethodNotExisting() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new SimpleTypeMethodsImpl(), SimpleTypeMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodOnlyInteger", "{\"param1\":true}")), new WriterOutputStream(outputString));


        Assert.assertEquals("-32602", getParamFromJson(outputString.toString(), "error.code"));
    }

    @Test
    public void testWithStructureParameterStrict() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodStrict", "{\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodStrictWithExtras_param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterStrictWithNull() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodStrict", null)), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodStrictWithExtras", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterStrictWithParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodStrict", "{\"param1\":\"something\",\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodStrictParam1WithExtras_param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterJson() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodJson", "{\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodJsonWithExtras_param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterJsonWithNull() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodJson", null)), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodJsonWithExtras", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterJsonWithParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodJson", "{\"param1\":\"something\",\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodJsonParam1WithExtras_param1param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterMap() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodMap", "{\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodMapWithExtras_param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterMapWithParameter() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodMap", "{\"param1\":\"something\",\"param2\":\"something\"}")), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodMapParam1WithExtras_param1param2", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testWithStructureParameterMapWithNull() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ParameterStructureMethodsImpl(), ParameterStructureMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethodMap", null)), new WriterOutputStream(outputString));


        Assert.assertEquals("testMethodMapWithExtras", getParamFromJson(outputString.toString(), "result"));
    }

    @Test
    public void testMethodWithUnknownException() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ExceptionMethodsImpl(), ExceptionMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":null}")), new WriterOutputStream(outputString));

        Assert.assertEquals("-32001", getParamFromJson(outputString.toString(), "error.code"));
        Assert.assertEquals(IllegalArgumentException.class.getName(), getParamFromJson(outputString.toString(), "error.data"));
    }

    @Test
    public void testMethodWithSomeException() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ExceptionMethodsImpl(), ExceptionMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":\"some\"}")), new WriterOutputStream(outputString));

        Assert.assertEquals("-32050", getParamFromJson(outputString.toString(), "error.code"));
        Assert.assertEquals("Some error", getParamFromJson(outputString.toString(), "error.message"));
    }

    @Test
    public void testMethodWithSomeOtherException() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ExceptionMethodsImpl(), ExceptionMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "testMethod", "{\"param1\":\"someOther\"}")), new WriterOutputStream(outputString));

        Assert.assertEquals("-32051", getParamFromJson(outputString.toString(), "error.code"));
        Assert.assertEquals("Some other error", getParamFromJson(outputString.toString(), "error.message"));
    }

    @Test
    public void testComplexNullResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class, false);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "nullMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertTrue(outputString.toString().length() > 0);
        Assert.assertNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "error"));
    }

    @Test
    public void testComplexNullResultNotReturningVoid() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class, false);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "nullMethod", "{}")), new WriterOutputStream(outputString));

        Assert.assertTrue(outputString.toString().length() > 0);
        Assert.assertNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "error"));
    }

    @Test
    public void testComplexVoidResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class, true);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "voidMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertTrue(outputString.toString().length() > 0);
        Assert.assertNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "error"));
    }

    @Test
    public void testComplexVoidResultNotReturningVoid() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class, false);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "voidMethod", "{}")), new WriterOutputStream(outputString));

        Assert.assertEquals(0, outputString.toString().length());
    }

    @Test
    public void testComplexEmptyResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "emptyMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "result.resultInteger"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "result.resultString"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "result.resultBoolean"));
        Assert.assertNull(getParamFromJson(outputString.toString(), "result.resultJson"));
    }

    @Test
    public void testComplexFilledResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "filledMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("1", getParamFromJson(outputString.toString(), "result.resultInteger"));
        Assert.assertEquals("2", getParamFromJson(outputString.toString(), "result.resultString"));
        Assert.assertEquals("true", getParamFromJson(outputString.toString(), "result.resultBoolean"));
        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result.resultJson"));
        Assert.assertEquals("3", getParamFromJson(outputString.toString(), "result.resultJson.attr1"));
        Assert.assertEquals("4", getParamFromJson(outputString.toString(), "result.resultJson.attr2"));
    }

    @Test
    public void testComplexNamedFilledResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "namedResultFilledMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("1", getParamFromJson(outputString.toString(), "result.result1.resultInteger"));
        Assert.assertEquals("2", getParamFromJson(outputString.toString(), "result.result1.resultString"));
        Assert.assertEquals("true", getParamFromJson(outputString.toString(), "result.result1.resultBoolean"));
        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result.result1.resultJson"));
        Assert.assertEquals("3", getParamFromJson(outputString.toString(), "result.result1.resultJson.attr1"));
        Assert.assertEquals("4", getParamFromJson(outputString.toString(), "result.result1.resultJson.attr2"));
    }

    @Test
    public void testComplexNamedResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "namedResultMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("namedResultMethod", getParamFromJson(outputString.toString(), "result.result1"));
    }

    @Test
    public void testComplexStringMapResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "stringMapMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("value1", getParamFromJson(outputString.toString(), "result.attr1"));
        Assert.assertEquals("value2", getParamFromJson(outputString.toString(), "result.attr2"));
    }

    @Test
    public void testComplexJsonMapResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "jsonMapMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("1", getParamFromJson(outputString.toString(), "result.attr1.attr3"));
        Assert.assertEquals("2", getParamFromJson(outputString.toString(), "result.attr2.attr4"));
    }

    @Test
    public void testComplexStringArrayResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "stringArrayMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("one", getParamFromJson(outputString.toString(), "result.0"));
        Assert.assertEquals("two", getParamFromJson(outputString.toString(), "result.1"));
        Assert.assertEquals("three", getParamFromJson(outputString.toString(), "result.2"));
    }

    @Test
    public void testComplexJsonArrayResult() throws IOException {
        StreamJsonRpcService service = new StreamJsonRpcService(new ComplexResultMethodsImpl(), ComplexResultMethods.class);
        StringWriter outputString = new StringWriter();

        service.handle(IOUtils.toInputStream(createJsonRequest("1", "jsonArrayMethod", "{}")), new WriterOutputStream(outputString));


        Assert.assertNotNull(getParamFromJson(outputString.toString(), "result"));
        Assert.assertEquals("one", getParamFromJson(outputString.toString(), "result.0.attr1"));
        Assert.assertEquals("two", getParamFromJson(outputString.toString(), "result.1.attr2"));
        Assert.assertEquals("three", getParamFromJson(outputString.toString(), "result.2.attr3"));
    }
}
