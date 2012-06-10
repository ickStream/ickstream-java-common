/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.service;

import com.ickstream.protocol.JsonRpcRequest;
import com.ickstream.protocol.JsonRpcResponse;
import org.apache.http.MethodNotSupportedException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractJsonRpcService {
    Map<String, Method> methodMap = new HashMap<String, Method>();
    Map<String, Class> parameterMap = new HashMap<String, Class>();
    ObjectMapper mapper;

    protected AbstractJsonRpcService() {
        for (Method method : getClass().getMethods()) {
            if (method.getParameterTypes().length <= 1) {
                methodMap.put(method.getName(), method);
                if (method.getParameterTypes().length == 1) {
                    parameterMap.put(method.getName(), method.getParameterTypes()[0]);
                }
            }
        }
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    private Boolean isSimpleType(Class type) {
        return Boolean.class.isAssignableFrom(type) || String.class.isAssignableFrom(type) || Number.class.isAssignableFrom(type);
    }

    public JsonRpcResponse invoke(JsonRpcRequest request) {
        Method method = methodMap.get(request.getMethod());
        Class parameterType = parameterMap.get(request.getMethod());
        try {
            Object result;
            if (parameterType != null) {
                Object params = null;
                if (method.getParameterAnnotations()[0].length > 0 && method.getParameterAnnotations()[0][0] instanceof ParamName) {
                    ParamName paramName = (ParamName) method.getParameterAnnotations()[0][0];
                    if (request.getParams() != null) {
                        params = mapper.readValue(request.getParams().get(paramName.value()), parameterType);
                    } else {
                        params = parameterType.newInstance();
                    }
                } else {
                    if (request.getParams() != null) {
                        params = mapper.readValue(request.getParams(), parameterType);
                    } else {
                        params = parameterType.newInstance();
                    }
                }
                result = method.invoke(this, params);
            } else if (method != null) {
                result = method.invoke(this);
            } else {
                throw new MethodNotSupportedException("Unknown method " + request.getMethod());
            }
            JsonNode jsonResult;
            ResultName resultName = method.getAnnotation(ResultName.class);
            if (resultName != null) {
                ObjectNode object = mapper.createObjectNode();
                object.put(resultName != null ? resultName.value() : "result", mapper.valueToTree(result));
                jsonResult = object;
            } else {
                jsonResult = mapper.valueToTree(result);
            }

            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            response.setResult(jsonResult);
            return response;
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof PlayerCommandException || e.getCause() instanceof IllegalArgumentException) {
                JsonRpcResponse response = new JsonRpcResponse();
                response.setId(request.getId());
                ObjectNode object = mapper.createObjectNode();
                object.put("code", -32602);
                object.put("message", e.getCause().getMessage());
                object.put("data", e.getCause().toString());
                response.setError(object);
                return response;
            } else if (e.getCause() != null) {
                JsonRpcResponse response = new JsonRpcResponse();
                response.setId(request.getId());
                ObjectNode object = mapper.createObjectNode();
                object.put("code", -32603);
                object.put("message", e.getCause().getMessage());
                object.put("data", e.getCause().toString());
                response.setError(object);
                return response;
            } else {
                JsonRpcResponse response = new JsonRpcResponse();
                response.setId(request.getId());
                ObjectNode object = mapper.createObjectNode();
                object.put("code", -32603);
                object.put("message", e.getMessage());
                object.put("data", e.toString());
                response.setError(object);
                return response;
            }
        } catch (JsonMappingException e) {
            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            ObjectNode object = mapper.createObjectNode();
            object.put("code", -32602);
            object.put("message", e.getMessage());
            object.put("data", e.toString());
            response.setError(object);
            return response;
        } catch (IllegalAccessException e) {
            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            ObjectNode object = mapper.createObjectNode();
            object.put("code", -32601);
            object.put("message", e.getMessage());
            object.put("data", e.toString());
            response.setError(object);
            return response;
        } catch (JsonParseException e) {
            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            ObjectNode object = mapper.createObjectNode();
            object.put("code", -32700);
            object.put("message", e.getMessage());
            object.put("data", e.toString());
            response.setError(object);
            return response;
        } catch (IOException e) {
            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            ObjectNode object = mapper.createObjectNode();
            object.put("code", -32603);
            object.put("message", e.getMessage());
            object.put("data", e.toString());
            response.setError(object);
            return response;
        } catch (MethodNotSupportedException e) {
            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            ObjectNode object = mapper.createObjectNode();
            object.put("code", -32601);
            object.put("message", e.getMessage());
            response.setError(object);
            return response;
        } catch (InstantiationException e) {
            JsonRpcResponse response = new JsonRpcResponse();
            response.setId(request.getId());
            ObjectNode object = mapper.createObjectNode();
            object.put("code", -32602);
            object.put("message", e.getMessage());
            object.put("data", e.toString());
            response.setError(object);
            return response;
        }
    }
}
