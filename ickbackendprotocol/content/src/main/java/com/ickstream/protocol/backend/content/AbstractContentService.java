/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.ickstream.protocol.backend.common.*;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.service.AccountInformation;
import com.ickstream.protocol.service.ImageReference;
import com.ickstream.protocol.service.content.*;
import com.ickstream.protocol.service.corebackend.CoreBackendService;
import com.ickstream.protocol.service.corebackend.DeviceResponse;
import com.ickstream.protocol.service.corebackend.UserResponse;
import com.ickstream.protocol.service.corebackend.UserServiceResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractContentService extends AbstractCloudService implements ContentService {
    @Inject
    BusinessLogger businessLogger;

    private List<ContentHandlerEntry> contentHandlers = new ArrayList<ContentHandlerEntry>();
    private List<ContentItemHandlerEntry> contentItemHandlers = new ArrayList<ContentItemHandlerEntry>();
    private List<ManagementItemHandlerEntry> managementItemHandlers = new ArrayList<ManagementItemHandlerEntry>();
    private List<ContentItem> topLevelItems = new ArrayList<ContentItem>();
    private List<ProtocolDescriptionContext> contexts = new ArrayList<ProtocolDescriptionContext>();
    private List<ManagementProtocolDescriptionContext> managementContexts = new ArrayList<ManagementProtocolDescriptionContext>();

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{(.*?)\\}");
    private static final Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

    protected abstract CoreBackendService getCoreBackendService();

    /**
     * Returns the unique identity of this service
     *
     * @return The identity of this service
     */
    protected abstract String getServiceId();

    protected List<ImageReference> getImages(String contextId) {
        return null;
    }

    protected AccountInformation getAccountInformation(UserServiceResponse userService) throws IOException {
        return null;
    }

    private synchronized Pattern getPattern(String patternString) {
        Pattern pattern = patternCache.get(patternString);
        if (pattern == null) {
            pattern = Pattern.compile(patternString);
            patternCache.put(patternString, pattern);
        }
        return pattern;
    }

    @Override
    public AccountInformation getAccountInformation() {
        BusinessCall businessCall = startBusinessCall("getAccountInformation");
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            String userId = InjectHelper.instance(RequestContext.class).getUserId();
            UserServiceResponse userService = null;
            if (deviceId != null) {
                DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
                if (device != null) {
                    userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                }
            } else if (userId != null) {
                UserResponse user = getCoreBackendService().getUserById(userId);
                if (user != null) {
                    userService = getCoreBackendService().getUserServiceByUser(userId);
                }
            }
            if (userService != null) {
                try {
                    AccountInformation result = getAccountInformation(userService);
                    businessLogger.logSuccessful(businessCall);
                    return result;
                } catch (UnsupportedEncodingException e) {
                    //TODO: How do we handle errors ?
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    //TODO: How do we handle errors ?
                    e.printStackTrace();
                } catch (IOException e) {
                    //TODO: How do we handle errors ?
                    e.printStackTrace();
                }
                return null;
            }
            throw new UnauthorizedAccessException();
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    protected void addManagementContext(String contextId, List<String> supportedTypes) {
        ManagementProtocolDescriptionContext context = new ManagementProtocolDescriptionContext(contextId);
        context.getSupportedTypes().addAll(supportedTypes);
        managementContexts.add(context);
    }

    protected void addContext(String contextId, String name) {
        contexts.add(new ProtocolDescriptionContext(contextId, name));
    }

    protected void addContext(String contextId, String name, List<ImageReference> images) {
        contexts.add(new ProtocolDescriptionContext(contextId, name, images));
    }

    protected void addHandler(ContentHandler handler, String contextId, String type) {
        addHandler(handler, contextId, type, null, null);
    }

    protected void addHandler(ContentHandler handler, String contextId, String type, List<ParameterValue> parameters) {
        addHandler(handler, contextId, type, null, parameters);
    }

    protected void addTopLevelItem(String id, String text, String parentNode, String descendContextId, String descendType) {
        addTopLevelItem(id, text, parentNode, descendContextId, descendType, null);
    }

    protected void addTopLevelItem(String id, String text, String parentNode, String descendContextId, String descendType, List<ParameterValue> descendParameters) {
        ContentItem item = new ContentItem();
        item.setId(id);
        item.setText(text);
        item.setType("menu");
        item.setParentNode(parentNode);
        topLevelItems.add(item);
    }

    protected void addManagementItemHandler(String attribute, String expression, ManagementItemHandler handler) {
        managementItemHandlers.add(new ManagementItemHandlerEntry(handler, attribute, expression));
    }

    protected void addItemHandler(String contextId, String attribute, String expression, ContentItemHandler handler) {
        String pattern = PARAMETER_PATTERN.matcher(expression).replaceAll("(.*?)");
        ContentItemHandlerEntry entry = new ContentItemHandlerEntry();
        entry.setContextId(contextId);
        entry.setAttribute(attribute);
        entry.setHandler(handler);
        entry.setPattern(pattern);

        Matcher m = PARAMETER_PATTERN.matcher(expression);
        while (m.find()) {
            entry.getParameters().add(m.group(1));
        }
        contentItemHandlers.add(entry);
    }

    protected void addHandler(ContentHandler handler, String contextId, String type, Integer maxSize, Collection<ParameterValue> parameters) {
        ContentHandlerEntry entry = new ContentHandlerEntry();
        entry.setHandler(handler);
        entry.setMaxSize(maxSize != null ? maxSize : 200);
        if (parameters != null) {
            entry.setParameters(new TreeSet<ParameterValue>(parameters));
        } else {
            entry.setParameters(new TreeSet<ParameterValue>());
        }
        List<String> supportedParameters = new ArrayList<String>();
        if (contextId != null) {
            supportedParameters.add("contextId");
            entry.getParameters().add(new ParameterValue("contextId", contextId));
        }
        if (type != null) {
            supportedParameters.add("type");
            entry.getParameters().add(new ParameterValue("type", type));
        }
        contentHandlers.add(entry);
        if (parameters != null) {
            for (ParameterValue parameter : parameters) {
                supportedParameters.add(parameter.getParameter());
            }
        }
        for (ProtocolDescriptionContext context : contexts) {
            if (contextId == null || context.getContextId().equals(contextId)) {
                RequestDescription request = new RequestDescription(type);
                boolean added = false;
                for (RequestDescription contextRequest : context.getSupportedRequests()) {
                    if ((contextRequest.getType() == null && type == null) ||
                            (type != null && type.equals(contextRequest.getType()))) {
                        request = contextRequest;
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    context.getSupportedRequests().add(request);
                }
                request.getParameters().add(supportedParameters);
            }
        }
    }

    protected abstract BusinessCall createBusinessCall(String serviceId, String deviceId, String deviceModel, String userId, String deviceAddress, String method);

    protected BusinessCall startBusinessCall(String serviceId, String method) {
        RequestContext requestContext = InjectHelper.instance(RequestContext.class);
        DeviceResponse device = null;
        if (requestContext.getDeviceId() != null) {
            device = getCoreBackendService().getDeviceById(requestContext.getDeviceId());
        }
        return createBusinessCall(serviceId, requestContext.getDeviceId(), device != null ? device.getModel() : null, device != null ? device.getUserId() : requestContext.getUserId(), requestContext.getDeviceAddress(), method);
    }

    protected BusinessCall startBusinessCall(String method) {
        return startBusinessCall(getServiceId(), method);
    }

    public GetManagementProtocolDescriptionResponse getManagementProtocolDescription(Integer offset, Integer count) {
        BusinessCall businessCall = startBusinessCall("getManagementProtocolDescription");
        businessCall.addParameter("offset", offset);
        businessCall.addParameter("count", count);
        try {
            offset = offset != null ? offset : 0;
            count = count != null ? count : managementContexts.size();

            GetManagementProtocolDescriptionResponse result = new GetManagementProtocolDescriptionResponse();
            result.setOffset(offset);
            result.setCountAll(managementContexts.size());

            for (int i = 0; i < managementContexts.size(); i++) {
                if (i >= offset && result.getItems().size() < count) {
                    ManagementProtocolDescriptionContext context = managementContexts.get(i);
                    ManagementProtocolDescriptionContext resultContext = new ManagementProtocolDescriptionContext(context.getContextId());
                    resultContext.setSupportedTypes(context.getSupportedTypes());
                    result.getItems().add(resultContext);
                }
            }

            result.setCount(result.getItems().size());
            businessLogger.logSuccessful(businessCall);
            return result;
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    @Override
    public GetProtocolDescriptionResponse getProtocolDescription(Integer offset, Integer count) {
        BusinessCall businessCall = startBusinessCall("getProtocolDescription");
        businessCall.addParameter("offset", offset);
        businessCall.addParameter("count", count);
        try {
            offset = offset != null ? offset : 0;
            count = count != null ? count : contexts.size();

            GetProtocolDescriptionResponse result = new GetProtocolDescriptionResponse();
            result.setOffset(offset);
            result.setCountAll(contexts.size());

            for (int i = 0; i < contexts.size(); i++) {
                if (i >= offset && result.getItems().size() < count) {
                    ProtocolDescriptionContext context = contexts.get(i);
                    ProtocolDescriptionContext resultContext = new ProtocolDescriptionContext(context.getContextId(), context.getName(), getImages(context.getContextId()));
                    resultContext.setSupportedRequests(context.getSupportedRequests());
                    result.getItems().add(resultContext);
                }
            }

            result.setCount(result.getItems().size());
            businessLogger.logSuccessful(businessCall);
            return result;
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    @Override
    public ContentItem getItem(String contextId, Map<String, String> parameters) {
        BusinessCall businessCall = startBusinessCall("getItem");
        businessCall.addParameters(parameters);
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
            if (device != null) {
                UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                if (userService != null) {
                    for (ContentItemHandlerEntry entry : contentItemHandlers) {
                        if (contextId == null || contextId.equals(entry.getContextId())) {
                            if (parameters.containsKey(entry.getAttribute())) {
                                Matcher m = getPattern(entry.getPattern()).matcher(parameters.get(entry.getAttribute()));
                                if (m.matches()) {
                                    m.reset();
                                    if (m.find()) {
                                        for (int i = 1; i <= m.groupCount(); i++) {
                                            String attributeValue = m.group(i);
                                            parameters.put(entry.getParameters().get(i - 1), attributeValue);
                                        }
                                    }
                                    ContentItem result = entry.getHandler().getItemOrCatchErrors(userService, parameters);
                                    businessLogger.logSuccessful(businessCall);
                                    return result;
                                }
                            }
                        }
                    }
                    businessLogger.logFailed(businessCall, "Invalid parameters");
                } else {
                    businessLogger.logFailed(businessCall, "Service not available for device");
                }
            } else {
                businessLogger.logFailed(businessCall, "Unknown device");
            }
            return null;
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    @Override
    public ContentResponse findTopLevelItems(Integer offset, Integer count) {
        BusinessCall businessCall = startBusinessCall("findTopLevelItems");
        businessCall.addParameter("offset", offset);
        businessCall.addParameter("count", count);
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            offset = offset != null ? offset : 0;
            DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
            if (device != null) {
                UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                if (userService != null) {
                    ContentResponse result = findTopLevelItems(userService, offset, count);
                    businessLogger.logSuccessful(businessCall);
                    return result;
                }
            }
            throw new UnauthorizedAccessException();
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    protected ContentResponse findTopLevelItems(UserServiceResponse userService, Integer offset, Integer count) {
        ContentResponse result = new ContentResponse();
        result.setOffset(offset);
        for (ContentItem item : topLevelItems) {
            if (offset <= 0 && (count == null || count > result.getItems().size())) {
                result.getItems().add(item);
            }
            offset--;
        }
        result.setCount(result.getItems().size());
        result.setCountAll(topLevelItems.size());
        return result;
    }

    @Override
    public Boolean addItem(String contextId, Map<String, String> parameters) {
        BusinessCall businessCall = startBusinessCall("addItem");
        businessCall.addParameters(parameters);
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
            if (userService != null) {
                Boolean result = addItem(userService, contextId, parameters);
                businessLogger.logSuccessful(businessCall);
                return result;
            }
            throw new UnauthorizedAccessException();
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    @Override
    public Boolean removeItem(String contextId, Map<String, String> parameters) {
        BusinessCall businessCall = startBusinessCall("removeItem");
        businessCall.addParameters(parameters);
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
            if (userService != null) {
                Boolean result = removeItem(userService, contextId, parameters);
                businessLogger.logSuccessful(businessCall);
                return result;
            }
            throw new UnauthorizedAccessException();
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    @Override
    public ContentResponse findItems(Integer offset, Integer count, String contextId, Map<String, String> parameters) {
        BusinessCall businessCall = startBusinessCall("findItems");
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            if (parameter.getKey().equalsIgnoreCase("count") || parameter.getKey().equalsIgnoreCase("offset")) {
                businessCall.addParameter(parameter.getKey(), !StringUtils.isEmpty(parameter.getValue()) ? Integer.valueOf(parameter.getValue()) : null);
            } else {
                businessCall.addParameter(parameter.getKey(), parameter.getValue());
            }
        }
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            if (offset == null) {
                offset = 0;
            }

            DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
            if (device != null) {
                UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                if (userService != null) {
                    ContentResponse result = findItems(userService, offset, count, contextId, parameters);
                    businessLogger.logSuccessful(businessCall);
                    return result;
                }
            }
            throw new UnauthorizedAccessException();
        } catch (RuntimeException e) {
            businessLogger.logFailed(businessCall, e);
            throw e;
        }
    }

    protected Boolean addItem(UserServiceResponse userService, String contextId, Map<String, String> parameters) {
        for (ManagementItemHandlerEntry entry : managementItemHandlers) {
            if (parameters.containsKey(entry.getAttribute())) {
                Matcher m = getPattern(entry.getPattern()).matcher(parameters.get(entry.getAttribute()));
                if (m.matches()) {
                    m.reset();
                    if (m.find()) {
                        String id = m.group(1);
                        return entry.getHandler().addItemOrCatchErrors(userService, contextId, id);
                    }
                }
            }
        }
        return false;
    }

    protected Boolean removeItem(UserServiceResponse userService, String contextId, Map<String, String> parameters) {
        for (ManagementItemHandlerEntry entry : managementItemHandlers) {
            if (parameters.containsKey(entry.getAttribute())) {
                Matcher m = getPattern(entry.getPattern()).matcher(parameters.get(entry.getAttribute()));
                if (m.matches()) {
                    m.reset();
                    if (m.find()) {
                        String id = m.group(1);
                        return entry.getHandler().removeItemOrCatchErrors(userService, contextId, id);
                    }
                }
            }
        }
        return false;
    }

    protected ContentResponse findItems(UserServiceResponse userService, Integer offset, Integer count, String contextId, Map<String, String> parameters) {
        ContentHandlerEntry foundHandler = null;
        Map<String, String> foundParameters = null;
        for (ContentHandlerEntry entry : contentHandlers) {
            boolean match = true;
            Map<String, String> adjustedParameters = new HashMap<String, String>(parameters);
            for (ParameterValue param : entry.getParameters()) {
                if (!parameters.containsKey(param.getParameter())) {
                    match = false;
                    break;
                } else if (param.getValue() != null) {
                    Matcher m = getPattern(param.getValue()).matcher(parameters.get(param.getParameter()));
                    if (m.matches()) {
                        m.reset();
                        if (param.getParameters().size() > 0 && m.find()) {
                            for (int i = 1; i <= m.groupCount(); i++) {
                                String attributeValue = m.group(i);
                                adjustedParameters.put(param.getParameters().get(i - 1), attributeValue);
                            }
                        }
                    } else {
                        match = false;
                        break;
                    }

                }
            }
            if (match && (foundHandler == null || foundHandler.getParameters().size() < entry.getParameters().size())) {
                foundHandler = entry;
                foundParameters = adjustedParameters;
            }
        }
        if (foundHandler != null) {
            if (foundHandler.getMaxSize() != null && (count == null || count > foundHandler.getMaxSize())) {
                count = foundHandler.getMaxSize();
            }
            ContentResponse result = foundHandler.getHandler().findItems(userService, foundParameters, offset, count);
            if (result.getCount() == null) {
                result.setCount(result.getItems().size());
            }
            return result;
        }
        throw new InvalidParameterException();
    }

    public static interface ContentHandler {
        ContentResponse findItems(UserServiceResponse userService, Map<String, String> parameters, Integer offset, Integer count);
    }

    public static interface ContentItemHandler {
        ContentItem getItemOrCatchErrors(UserServiceResponse userService, Map<String, String> parameters);
    }

    public static interface ManagementItemHandler {
        Boolean addItemOrCatchErrors(UserServiceResponse userService, String contextId, String itemId);

        Boolean removeItemOrCatchErrors(UserServiceResponse userService, String contextId, String itemId);
    }

    public static abstract class BusinessCallHandler {
        protected abstract CoreBackendService getCoreBackendService();

        protected abstract BusinessCall createBusinessCall(String serviceId, String deviceId, String deviceModel, String userId, String deviceAddress, String method);

        protected BusinessCall startBusinessCall(String service, String method) {
            RequestContext requestContext = InjectHelper.instance(RequestContext.class);
            DeviceResponse device = null;
            if (requestContext.getDeviceId() != null) {
                device = getCoreBackendService().getDeviceById(requestContext.getDeviceId());
            }
            return createBusinessCall(service, requestContext.getDeviceId(), device != null ? device.getModel() : null, device != null ? device.getUserId() : requestContext.getUserId(), requestContext.getDeviceAddress(), method);
        }

        protected void logSuccessfulBusinessCall(BusinessCall businessCall) {
            InjectHelper.instance(BusinessLogger.class).logSuccessful(businessCall);
        }

        protected void logFailedBusinessCall(BusinessCall businessCall, String error) {
            InjectHelper.instance(BusinessLogger.class).logFailed(businessCall, error);
        }

        protected void logFailedBusinessCall(BusinessCall businessCall, String error, Throwable exception) {
            InjectHelper.instance(BusinessLogger.class).logFailed(businessCall, error, exception);
        }

        protected void logFailedBusinessCall(BusinessCall businessCall, Throwable exception) {
            InjectHelper.instance(BusinessLogger.class).logFailed(businessCall, exception);
        }
    }

    public static abstract class AbstractManagementItemHandler extends BusinessCallHandler implements ManagementItemHandler {
        @Override
        public Boolean addItemOrCatchErrors(UserServiceResponse userService, String contextId, String itemId) {
            try {
                return addItem(userService, contextId, itemId);
            } catch (UnsupportedEncodingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (IOException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public Boolean removeItemOrCatchErrors(UserServiceResponse userService, String contextId, String itemId) {
            try {
                return removeItem(userService, contextId, itemId);
            } catch (UnsupportedEncodingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (IOException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            }

            return false;
        }

        protected abstract Boolean addItem(UserServiceResponse userService, String contextId, String itemId) throws IOException;

        protected abstract Boolean removeItem(UserServiceResponse userService, String contextId, String itemId) throws IOException;
    }

    public static abstract class AbstractContentItemHandler extends BusinessCallHandler implements ContentItemHandler {
        @Override
        public ContentItem getItemOrCatchErrors(UserServiceResponse userService, Map<String, String> parameters) {
            try {
                return getItem(userService, parameters);
            } catch (UnsupportedEncodingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (IOException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            }

            return null;
        }

        protected abstract ContentItem getItem(UserServiceResponse userService, Map<String, String> parameters) throws IOException;
    }

    public static abstract class AbstractContentHandler extends BusinessCallHandler implements ContentHandler {
        protected Long EXPIRATION_TIMESTAMP_HOUR = 3600l;
        protected Long EXPIRATION_TIMESTAMP_DAY = 24l * 3600;
        protected Long EXPIRATION_TIMESTAMP_WEEK = 7l * 24 * 3600;

        @Override
        public ContentResponse findItems(UserServiceResponse userService, Map<String, String> parameters, Integer offset, Integer count) {
            ContentResponse result = new ContentResponse();
            result.setOffset(offset);

            try {
                result = findItems(userService, parameters, offset, count, result);
            } catch (UnsupportedEncodingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            } catch (IOException e) {
                //TODO: How do we handle errors ?
                e.printStackTrace();
            }

            return result;
        }

        protected abstract ContentResponse findItems(UserServiceResponse userService, Map<String, String> parameters, Integer offset, Integer count, ContentResponse result) throws IOException;

        protected Long getExpirationTimestamp(Long expirationPeriod) {
            return System.currentTimeMillis() / 1000 + expirationPeriod;
        }

    }

    private class ManagementItemHandlerEntry {
        private ManagementItemHandler handler;
        private String pattern;
        private String attribute;

        private ManagementItemHandlerEntry(ManagementItemHandler handler, String attribute, String pattern) {
            this.handler = handler;
            this.pattern = pattern;
            this.attribute = attribute;
        }

        public ManagementItemHandler getHandler() {
            return handler;
        }

        public String getPattern() {
            return pattern;
        }

        public String getAttribute() {
            return attribute;
        }
    }

    private class ContentItemHandlerEntry {
        private ContentItemHandler handler;
        private String contextId;
        private String attribute;
        private String pattern;
        private List<String> parameters = new ArrayList<String>();

        public ContentItemHandler getHandler() {
            return handler;
        }

        public void setHandler(ContentItemHandler handler) {
            this.handler = handler;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }

        public String getContextId() {
            return contextId;
        }

        public void setContextId(String contextId) {
            this.contextId = contextId;
        }
    }

    private class ContentHandlerEntry {
        private ContentHandler handler;
        private String pattern;
        private Integer maxSize;
        private Set<ParameterValue> parameters = new TreeSet<ParameterValue>();

        public ContentHandler getHandler() {
            return handler;
        }

        public void setHandler(ContentHandler handler) {
            this.handler = handler;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }

        public Set<ParameterValue> getParameters() {
            return parameters;
        }

        public void setParameters(Set<ParameterValue> parameters) {
            this.parameters = parameters;
        }
    }

    public class ParameterValue implements Comparable<ParameterValue> {
        private String parameter;
        private String value;
        private List<String> parameters = new ArrayList<String>();

        public ParameterValue(String parameter, String value) {
            this.parameter = parameter;
            this.value = value;
            if (value != null && value.contains("{")) {
                Matcher m = PARAMETER_PATTERN.matcher(value);
                while (m.find()) {
                    parameters.add(m.group(1));
                }
                this.value = PARAMETER_PATTERN.matcher(value).replaceAll("(.*?)");
            }
        }

        public ParameterValue(String parameter) {
            this(parameter, null);
        }

        public String getParameter() {
            return parameter;
        }

        public String getValue() {
            return value;
        }

        public List<String> getParameters() {
            return parameters;
        }

        @Override
        public int compareTo(ParameterValue parameterValue) {
            return parameter.compareTo(parameterValue.getParameter());
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ParameterValue)) return false;
            return new EqualsBuilder().append(parameter, ((ParameterValue) o).getParameter()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(parameter).hashCode();
        }
    }
}
