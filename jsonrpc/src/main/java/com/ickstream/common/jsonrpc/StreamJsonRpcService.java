/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class StreamJsonRpcService {
    private Object serviceImplementation;
    private Class serviceInterface;
    private ObjectMapper mapper;
    private static final Map<String, List<Method>> methodCache = new HashMap<String, List<Method>>();
    private Boolean returnOnVoid;
    private Boolean ignoreResponses;
    private MessageLogger messageLogger;

    public <T> StreamJsonRpcService(T serviceImplementation, Class<T> serviceInterface) {
        this(serviceImplementation, serviceInterface, false, true);
    }

    public <I, T extends I> StreamJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid) {
        this(serviceImplementation, serviceInterface, returnOnVoid, true);
    }

    public <I, T extends I> StreamJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid, Boolean ignoreResponses) {
        this.serviceImplementation = serviceImplementation;
        this.serviceInterface = serviceInterface;
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.returnOnVoid = returnOnVoid;
        this.ignoreResponses = ignoreResponses;
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    protected void handle(InputStream input, OutputStream ops) {
        String id = null;
        String version = null;

        JsonRpcRequest request = null;
        try {
            request = mapper.readValue(input, JsonRpcRequest.class);
        } catch (IOException e) {
            JsonRpcResponse response = new JsonRpcResponse("2.0", null);
            response.setError(new JsonRpcResponse.Error(JsonRpcError.INVALID_JSON, "Invalid JSON"));
            if (messageLogger != null) {
                messageLogger.onOutgoingMessage(null, mapper.valueToTree(response).toString());
            }
            try {
                mapper.writeValue(ops, response);
            } catch (IOException e2) {
                e2.printStackTrace();
                throw new RuntimeException(e2);
            }
            return;
        }
        JsonRpcResponse.Error error = null;
        JsonNode result = null;

        if (messageLogger != null) {
            messageLogger.onIncomingMessage(null, mapper.valueToTree(request).toString());
        }
        if (StringUtils.isEmpty(request.getJsonrpc()) || StringUtils.isEmpty(request.getMethod())) {
            if (ignoreResponses && StringUtils.isEmpty(request.getMethod())) {
                // Just ignore responses which doesn't have a "method" attribute
                return;
            }
            JsonRpcResponse response = new JsonRpcResponse(
                    !StringUtils.isEmpty(request.getJsonrpc()) ? request.getJsonrpc() : "2.0",
                    !StringUtils.isEmpty(request.getId()) ? request.getId() : null
            );
            response.setError(new JsonRpcResponse.Error(JsonRpcError.INVALID_REQUEST, "Invalid Request"));
            if (messageLogger != null) {
                messageLogger.onOutgoingMessage(null, mapper.valueToTree(response).toString());
            }
            try {
                mapper.writeValue(ops, response);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return;
        }

        version = request.getJsonrpc();
        String methodName = request.getMethod();
        id = request.getId();
        JsonNode paramsNode = request.getParams();

        List<Method> methods = getMethods(methodName);

        if (methods.size() == 0) {
            JsonRpcResponse response = new JsonRpcResponse(version, id);
            response.setError(new JsonRpcResponse.Error(JsonRpcError.METHOD_NOT_FOUND, "Method not found"));
            if (messageLogger != null) {
                messageLogger.onOutgoingMessage(null, mapper.valueToTree(response).toString());
            }
            try {
                mapper.writeValue(ops, response);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return;
        }

        Method matchedMethod = null;
        Method matchingMethodWithOptionalParameters = null;

        for (Method method : methods) {
            boolean match = true;
            boolean exactMatch = true;
            int i = 0;
            for (Annotation[] annotations : method.getParameterAnnotations()) {
                boolean foundParameter = false;
                boolean optionalParameter = false;
                for (Annotation annotation : annotations) {
                    if (annotation instanceof JsonRpcParam) {
                        String name = ((JsonRpcParam) annotation).name();
                        if (paramsNode != null && paramsNode.has(name) && !paramsNode.get(name).isNull()) {
                            Class cls = method.getParameterTypes()[i];
                            if (Number.class.isAssignableFrom(cls)) {
                                if (paramsNode.get(name).isNumber()) {
                                    foundParameter = true;
                                }
                            } else if (String.class.isAssignableFrom(cls)) {
                                if (paramsNode.get(name).isTextual()) {
                                    foundParameter = true;
                                }
                            } else if (Boolean.class.isAssignableFrom(cls)) {
                                if (paramsNode.get(name).isBoolean()) {
                                    foundParameter = true;
                                }
                            } else if (Collection.class.isAssignableFrom(cls)) {
                                if (paramsNode.get(name).isArray()) {
                                    foundParameter = true;
                                }
                            } else {
                                if (paramsNode.get(name).isObject()) {
                                    foundParameter = true;
                                }
                            }

                        } else {
                            foundParameter = false;
                            if (((JsonRpcParam) annotation).optional()) {
                                optionalParameter = true;
                            }
                        }
                        break;
                    } else if (annotation instanceof JsonRpcParamStructure) {
                        foundParameter = false;
                        optionalParameter = true;
                    }
                }
                if (!foundParameter) {
                    if (!optionalParameter) {
                        match = false;
                        break;
                    }
                    exactMatch = false;
                }
                i++;
            }
            if (exactMatch && match) {
                if ((matchedMethod == null || method.getParameterTypes().length > matchedMethod.getParameterTypes().length)) {
                    if ((paramsNode == null && method.getParameterTypes().length == 0) || (method.getParameterTypes().length == paramsNode.size())) {
                        matchedMethod = method;
                    } else {
                        exactMatch = false;
                    }
                }
            }
            if (!exactMatch && match) {
                if (matchingMethodWithOptionalParameters == null || method.getParameterTypes().length > matchingMethodWithOptionalParameters.getParameterTypes().length) {
                    matchingMethodWithOptionalParameters = method;
                }
            }
        }
        if (matchedMethod == null && matchingMethodWithOptionalParameters != null) {
            matchedMethod = matchingMethodWithOptionalParameters;
        }
        Class returnType = null;
        if (matchedMethod != null) {
            returnType = matchedMethod.getReturnType();
            List<JsonNode> parameters = getInputAgumentsForMethod(matchedMethod, paramsNode);
            try {
                result = invokeMethod(matchedMethod, parameters);
                if (id == null) {
                    // This is a notification, let's ignore the result
                    return;
                }
                JsonRpcResult resultName = matchedMethod.getAnnotation(JsonRpcResult.class);
                if (resultName != null && resultName.value() != null && resultName.value().length() > 0) {
                    ObjectNode node = mapper.createObjectNode();
                    node.put(resultName.value(), result);
                    result = node;
                }

            } catch (Throwable e) {
                if (InvocationTargetException.class.isInstance(e)) {
                    e = InvocationTargetException.class.cast(e).getTargetException();
                }
                if (matchedMethod.isAnnotationPresent(JsonRpcErrors.class)) {
                    JsonRpcErrors errorMappings = matchedMethod.getAnnotation(JsonRpcErrors.class);
                    for (JsonRpcError jsonRpcError : errorMappings.value()) {
                        if (jsonRpcError.exception().isInstance(e)) {
                            if (!jsonRpcError.data().equals("")) {
                                error = new JsonRpcResponse.Error(jsonRpcError.code(), jsonRpcError.message(), jsonRpcError.data());
                            } else if (e.getMessage() != null && e.getMessage().length() > 0) {
                                error = new JsonRpcResponse.Error(jsonRpcError.code(), jsonRpcError.message(), e.getMessage());
                            } else {
                                error = new JsonRpcResponse.Error(jsonRpcError.code(), jsonRpcError.message());
                            }
                            break;
                        }
                    }
                }
                if (error == null) {
                    if (e.getMessage() == null) {
                        error = new JsonRpcResponse.Error(JsonRpcError.SERVICE_ERROR, e.getMessage(), e.getClass().getName());
                    } else {
                        StringWriter stringWriter = new StringWriter();
                        PrintWriter writer = new PrintWriter(stringWriter);
                        e.printStackTrace(writer);
                        error = new JsonRpcResponse.Error(JsonRpcError.SERVICE_ERROR, stringWriter.toString(), e.getClass().getName());
                    }
                }
            }
            if (id == null) {
                //This is a notification, let's ignore the result
                return;
            }
        } else {
            error = new JsonRpcResponse.Error(JsonRpcError.INVALID_PARAMS, "Invalid parameter list for method " + methodName, paramsNode != null ? paramsNode.toString() : null);
        }
        JsonRpcResponse response = new JsonRpcResponse(version, id);
        if (error == null) {
            if (result != null || !returnType.getName().equals("void") || returnOnVoid) {
                response.setResult(result);
            } else {
                response = null;
            }
        } else if (error != null) {
            response.setError(error);
        }

        if (response != null) {
            if (messageLogger != null) {
                messageLogger.onOutgoingMessage(null, mapper.valueToTree(response).toString());
            }
            // write it
            try {
                mapper.writeValue(ops, response);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private List<JsonNode> getInputAgumentsForMethod(Method method, JsonNode paramsNode) {
        List<JsonNode> params = new ArrayList<JsonNode>();
        int i = 0;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            String name = null;
            boolean structure = false;
            for (Annotation annotation : annotations) {
                if (annotation instanceof JsonRpcParam) {
                    name = ((JsonRpcParam) annotation).name();
                } else if (annotation instanceof JsonRpcParamStructure) {
                    structure = true;
                }
            }
            if (name != null && paramsNode != null && paramsNode.has(name)) {
                params.add(paramsNode.get(name));
            } else if (structure) {
                if (paramsNode != null) {
                    params.add(paramsNode);
                } else {
                    params.add(mapper.createObjectNode());
                }
            } else {
                params.add(null);
            }
            i++;

        }
        return params;
    }

    private JsonNode invokeMethod(Method method, List<JsonNode> params) throws IOException, InvocationTargetException, IllegalAccessException {
        Object[] convertedParams = new Object[params.size()];
        Type[] parameterTypes = method.getGenericParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {
            if (params.get(i) != null) {
                convertedParams[i] = mapper.readValue(
                        params.get(i).traverse(), mapper.getTypeFactory().constructType(parameterTypes[i]));
            } else {
                convertedParams[i] = null;
            }
        }

        Object result = method.invoke(serviceImplementation, convertedParams);
        return (method.getGenericReturnType() != null) ? mapper.valueToTree(result) : null;
    }

    private List<Method> getMethods(String name) {
        synchronized (methodCache) {
            String cacheKey = serviceImplementation.getClass().getName() + "." + name;
            List<Method> methods = methodCache.get(cacheKey);
            if (methods == null) {
                methods = new ArrayList<Method>();
                for (Method method : serviceInterface.getMethods()) {
                    if (method.getName().equals(name)) {
                        methods.add(method);
                    }
                }
                methodCache.put(cacheKey, methods);
            }
            return methods;
        }
    }
}
