/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.node.ObjectNode;

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
    private MessageLogger messageLogger;

    public <T> StreamJsonRpcService(T serviceImplementation, Class<T> serviceInterface) {
        this(serviceImplementation, serviceInterface, false);
    }

    public <I, T extends I> StreamJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid) {
        this.serviceImplementation = serviceImplementation;
        this.serviceInterface = serviceInterface;
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.returnOnVoid = returnOnVoid;
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    protected void handle(InputStream input, OutputStream ops) {
        try {
            JsonRpcRequest request = mapper.readValue(input, JsonRpcRequest.class);
            JsonRpcResponse.Error error = null;
            JsonNode result = null;

            if (messageLogger != null) {
                messageLogger.onIncomingMessage(null, mapper.valueToTree(request).toString());
            }
            if (StringUtils.isEmpty(request.getJsonrpc()) || StringUtils.isEmpty(request.getMethod())) {
                JsonRpcResponse response = new JsonRpcResponse(
                        !StringUtils.isEmpty(request.getJsonrpc()) ? request.getJsonrpc() : "2.0",
                        !StringUtils.isEmpty(request.getId()) ? request.getId() : null
                );
                response.setError(new JsonRpcResponse.Error(-32600, "Invalid Request"));
                if (messageLogger != null) {
                    messageLogger.onOutgoingMessage(null, mapper.valueToTree(response).toString());
                }
                mapper.writeValue(ops, response);
                return;
            }

            String version = request.getJsonrpc();
            String methodName = request.getMethod();
            String id = request.getId();
            JsonNode paramsNode = request.getParams();

            List<Method> methods = getMethods(methodName);

            if (methods.size() == 0) {
                JsonRpcResponse response = new JsonRpcResponse(version, id);
                response.setError(new JsonRpcResponse.Error(-32601, "Method not found"));
                if (messageLogger != null) {
                    messageLogger.onOutgoingMessage(null, mapper.valueToTree(response).toString());
                }
                mapper.writeValue(ops, response);
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
                            error = new JsonRpcResponse.Error(0, e.getMessage(), e.getClass().getName());
                        } else {
                            StringWriter stringWriter = new StringWriter();
                            PrintWriter writer = new PrintWriter(stringWriter);
                            e.printStackTrace(writer);
                            error = new JsonRpcResponse.Error(0, stringWriter.toString(), e.getClass().getName());
                        }
                    }
                }
                if (id == null) {
                    //This is a notification, let's ignore the result
                    return;
                }
            } else {
                error = new JsonRpcResponse.Error(-32602, "Invalid parameter list ofr method", null);
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
                mapper.writeValue(ops, response);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
            //TODO: Error handling
        } catch (JsonParseException e) {
            e.printStackTrace();
            //TODO: Error handling
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: Error handling
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
                        params.get(i), mapper.getTypeFactory().constructType(parameterTypes[i]));
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
