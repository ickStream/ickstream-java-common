/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.ickstream.common.jsonrpc.JsonHelper;
import com.ickstream.protocol.backend.common.*;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.common.data.StreamingReference;
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

    protected static final String PROTOCOL_VERSION = "2.0";
    protected static final String PROTOCOL_VERSION_1_0 = "1.0";
    protected static final String PROTOCOL_VERSION_2_0 = "2.0";

    private String version = PROTOCOL_VERSION;
    private List<ContentHandlerEntry> contentHandlers = new ArrayList<ContentHandlerEntry>();
    private List<ContentItemHandlerEntry> contentItemHandlers = new ArrayList<ContentItemHandlerEntry>();
    private List<ManagementItemHandlerEntry> managementItemHandlers = new ArrayList<ManagementItemHandlerEntry>();
    private List<DynamicPlaylistHandlerEntry> dynamicPlaylistHandlers = new ArrayList<DynamicPlaylistHandlerEntry>();
    private List<ProtocolDescriptionContextEntry> contexts = new ArrayList<ProtocolDescriptionContextEntry>();
    private List<PreferredMenuItemEntry> preferredMenus = new ArrayList<PreferredMenuItemEntry>();
    private List<ManagementProtocolDescriptionContext> managementContexts = new ArrayList<ManagementProtocolDescriptionContext>();

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{(.*?)\\}");
    private static final Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

    protected AbstractContentService() {
    }

    protected AbstractContentService(String version) {
        this.version = version;
    }

    protected String getVersion() {
        return version;
    }

    protected static enum Context {
        MY_MUSIC("myMusic"),
        ALL_MUSIC("allMusic"),
        HOT_MUSIC("hotMusic"),
        FRIENDS_MUSIC("friendsMusic"),
        ALL_RADIO("allRadio");

        private String name;

        private Context(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    ;

    private static class ProtocolDescriptionContextEntry {
        private String contextId;
        private String resourceBundle;
        private String nameKey;
        private List<ImageReference> images = new ArrayList<ImageReference>();
        private List<RequestDescription> supportedRequests = new ArrayList<RequestDescription>();
        private Map<String, Map<String, RequestDescription2>> supportedRequests2 = new HashMap<String, Map<String, RequestDescription2>>();

        private ProtocolDescriptionContextEntry(String contextId, String resourceBundle, String nameKey) {
            this.contextId = contextId;
            this.resourceBundle = resourceBundle;
            this.nameKey = nameKey;
        }

        private ProtocolDescriptionContextEntry(String contextId, String resourceBundle, String nameKey, List<ImageReference> images) {
            this(contextId, resourceBundle, nameKey);
            this.images = images;
        }


        public String getContextId() {
            return contextId;
        }

        public String getResourceBundle() {
            return resourceBundle;
        }

        public String getNameKey() {
            return nameKey;
        }

        public List<ImageReference> getImages() {
            return images;
        }

        public List<RequestDescription> getSupportedRequests() {
            return supportedRequests;
        }

        public Map<String, Map<String, RequestDescription2>> getSupportedRequests2() {
            return supportedRequests2;
        }
    }

    ;

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

    protected void addManagementContext(Context context, List<String> supportedTypes) {
        addManagementContext(context.toString(), supportedTypes);
    }

    protected void addManagementContext(String contextId, List<String> supportedTypes) {
        ManagementProtocolDescriptionContext context = new ManagementProtocolDescriptionContext(contextId);
        context.getSupportedTypes().addAll(supportedTypes);
        managementContexts.add(context);
    }

    protected void addPreferredMenu(PreferredMenuItemEntry hierarchy) {
        preferredMenus.add(hierarchy);
    }

    protected void addContext(Context context) {
        contexts.add(new ProtocolDescriptionContextEntry(context.toString(), null, "com.ickstream.protocol.backend.content.context." + context.toString()));
    }

    protected void addContext(String contextId, String resourceBundle, String nameKey) {
        contexts.add(new ProtocolDescriptionContextEntry(contextId, resourceBundle, nameKey));
    }

    protected void addContext(Context context, List<ImageReference> images) {
        contexts.add(new ProtocolDescriptionContextEntry(context.toString(), null, "com.ickstream.protocol.backend.content.context." + context.toString(), images));
    }

    protected void addContext(String contextId, String resourceBundle, String nameKey, List<ImageReference> images) {
        contexts.add(new ProtocolDescriptionContextEntry(contextId, resourceBundle, nameKey, images));
    }

    @Deprecated
    protected void addHandler(ContentHandler handler, Context context, String type) {
        addHandler(handler, context.toString(), type, null, null);
    }

    @Deprecated
    protected void addHandler(ContentHandler handler, String contextId, String type) {
        addHandler(handler, contextId, type, null, null);
    }

    @Deprecated
    protected void addHandler(ContentHandler handler, Context context, String type, List<ParameterValue> parameters) {
        addHandler(handler, context.toString(), type, null, parameters);
    }

    @Deprecated
    protected void addHandler(ContentHandler handler, String contextId, String type, List<ParameterValue> parameters) {
        addHandler(handler, contextId, type, null, parameters);
    }

    protected void addHandler(String id, ContentHandler handler, Context context, String type) {
        addHandler(id, handler, context.toString(), type, null, null);
    }

    protected void addHandler(String id, ContentHandler handler, String contextId, String type) {
        addHandler(id, handler, contextId, type, null, null);
    }

    protected void addHandler(String id, ContentHandler handler, Context context, String type, List<ParameterValue> parameters) {
        addHandler(id, handler, context.toString(), type, null, parameters);
    }

    protected void addHandler(String id, ContentHandler handler, String contextId, String type, List<ParameterValue> parameters) {
        addHandler(id, handler, contextId, type, null, parameters);
    }

    protected void addDynamicPlaylistHandler(ContentHandler handler, Context context, String type, List<ParameterValue> parameters) {
        addHandler(null, handler, context.toString(), type, null, parameters);
    }

    protected void addDynamicPlaylistHandler(ContentHandler handler, String contextId, String type, List<ParameterValue> parameters) {
        addHandler(null, handler, contextId, type, null, parameters);
    }

    protected void addManagementItemHandler(String attribute, String expression, ManagementItemHandler handler) {
        managementItemHandlers.add(new ManagementItemHandlerEntry(handler, attribute, expression));
    }

    protected void addItemHandler(Context context, String attribute, String expression, ContentItemHandler handler) {
        addItemHandler(context.toString(), attribute, expression, handler);
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

    @Deprecated
    protected void addHandler(ContentHandler handler, Context context, String type, Integer maxSize, Collection<ParameterValue> parameters) {
        addHandler(handler, context.toString(), type, maxSize, parameters);
    }

    protected void addHandler(String id, ContentHandler handler, Context context, String type, Integer maxSize, Collection<ParameterValue> parameters) {
        addHandler(id, handler, context.toString(), type, maxSize, parameters);
    }

    @Deprecated
    protected void addHandler(ContentHandler handler, String contextId, String type, Integer maxSize, Collection<ParameterValue> parameters) {
        addHandler(null, handler, contextId, type, maxSize, parameters);
    }

    protected void addHandler(String id, ContentHandler handler, String contextId, String type, Integer maxSize, Collection<ParameterValue> parameters) {
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
        JsonNode values = null;
        if (parameters != null) {
            Map<String, String> parameterValues = new HashMap<String, String>();
            for (ParameterValue parameter : parameters) {
                supportedParameters.add(parameter.getParameter());
                if (!parameter.getParameter().equals("contextId") && !parameter.getParameter().equals("type") && parameter.getValue() != null && parameter.isPredefined()) {
                    parameterValues.put(parameter.getParameter(), parameter.getValue());
                }
            }
            if (parameterValues.size() > 0) {
                values = new JsonHelper().objectToJson(parameterValues);
            }
        }
        for (ProtocolDescriptionContextEntry context : contexts) {
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
                String typeKey = "none";
                if (type != null) {
                    typeKey = type;
                }
                if (!context.getSupportedRequests2().containsKey(typeKey)) {
                    context.getSupportedRequests2().put(typeKey, new HashMap<String, RequestDescription2>());
                }
                if (id != null) {
                    if (values != null) {
                        context.getSupportedRequests2().get(typeKey).put(id, new RequestDescription2(supportedParameters, values));
                    } else {
                        context.getSupportedRequests2().get(typeKey).put(id, new RequestDescription2(supportedParameters));
                    }
                } else {
                    if (values != null) {
                        context.getSupportedRequests2().get(typeKey).put(handler.getClass().getSimpleName(), new RequestDescription2(supportedParameters, values));
                    } else {
                        context.getSupportedRequests2().get(typeKey).put(handler.getClass().getSimpleName(), new RequestDescription2(supportedParameters));
                    }
                }
            }
        }
    }

    protected void addHandler(DynamicPlaylistHandler handler, String type, Collection<ParameterValue> parameters) {
        DynamicPlaylistHandlerEntry entry = new DynamicPlaylistHandlerEntry();
        entry.setHandler(handler);
        entry.setType(type);
        if (parameters != null) {
            entry.setParameters(new TreeSet<ParameterValue>(parameters));
        } else {
            entry.setParameters(new TreeSet<ParameterValue>());
        }
        dynamicPlaylistHandlers.add(entry);
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


    /**
     * Initialize dynamic preferred menus to be returned from {@link #getPreferredMenus(Integer, Integer, String)} method.
     * The default implementation doesn't provide any dynamic preferred menus, override this function if
     * the service need to request information from the backend service to get dynamic preferred menus.
     *
     * @param userService The user service related to this request
     * @param language    The language to use for the menus
     */
    protected void initializeDynamicPreferredMenus(UserServiceResponse userService, String language) {
        // Do nothing
    }

    protected void initializeDynamicPreferredMenus(String language) {
        String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
        DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
        if (device != null) {
            UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
            if (userService != null) {
                initializeDynamicPreferredMenus(userService, language);
            }
        }
    }

    @Override
    public GetPreferredMenusResponse getPreferredMenus(Integer offset, Integer count, String language) {
        BusinessCall businessCall = startBusinessCall("getPreferredMenus");
        businessCall.addParameter("offset", offset);
        businessCall.addParameter("count", count);
        businessCall.addParameter("language", language);
        try {
            initializeDynamicPreferredMenus(language);

            offset = offset != null ? offset : 0;
            count = count != null ? count : preferredMenus.size();


            GetPreferredMenusResponse result = new GetPreferredMenusResponse();
            result.setOffset(offset);
            result.setCountAll(preferredMenus.size());

            for (int i = 0; i < preferredMenus.size(); i++) {
                if (i >= offset && result.getItems().size() < count) {
                    PreferredMenuItemEntry hierarchy = preferredMenus.get(i);
                    result.getItems().add(convertPreferredMenuEntryToItem(hierarchy, language));
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

    private <T extends AbstractPreferredMenu> T fillPreferredMenuChildItemOrRequest(T itemRequest, AbstractPreferredMenuItemEntry entry, String language) {
        if (entry.getChildItems() != null && entry.getChildItems().size() > 0) {
            itemRequest.setChildItems(new ArrayList<PreferredMenuItem>(entry.getChildItems().size()));
            for (PreferredMenuItemEntry child : entry.getChildItems()) {
                itemRequest.getChildItems().add(convertPreferredMenuEntryToItem(child, language));
            }
        } else if (entry.getChildRequest() != null) {
            itemRequest.setChildRequest(convertPreferredMenuEntryToRequest(entry.getChildRequest(), language));
        }
        return itemRequest;
    }

    private PreferredMenuRequest convertPreferredMenuEntryToRequest(PreferredMenuRequestEntry menuItemEntry, String language) {
        PreferredMenuRequest request = new PreferredMenuRequest(menuItemEntry.getRequest());
        return fillPreferredMenuChildItemOrRequest(request, menuItemEntry, language);
    }

    private PreferredMenuItem convertPreferredMenuEntryToItem(PreferredMenuItemEntry menuItemEntry, String language) {
        PreferredMenuItem item = new PreferredMenuItem();
        if (menuItemEntry.getType() != null) {
            item.setType(menuItemEntry.getType().toString());
        }
        item.setImages(menuItemEntry.getImages());
        String bundleId = menuItemEntry.getResourceBundle();
        if (bundleId == null) {
            bundleId = "ContentService";
        }
        if (language == null) {
            language = Locale.ENGLISH.getLanguage();
        }
        if (menuItemEntry.getTextKey() != null) {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleId, new Locale(language));
            item.setText(bundle.getString(menuItemEntry.getTextKey()));
        } else {
            item.setText(menuItemEntry.getTextValue());
        }
        return fillPreferredMenuChildItemOrRequest(item, menuItemEntry, language);
    }

    @Override
    @Deprecated
    public GetProtocolDescriptionResponse getProtocolDescription(Integer offset, Integer count, String language) {
        BusinessCall businessCall = startBusinessCall("getProtocolDescription");
        businessCall.addParameter("offset", offset);
        businessCall.addParameter("count", count);
        businessCall.addParameter("language", language);
        try {
            initializeDynamicContexts(language);

            offset = offset != null ? offset : 0;
            count = count != null ? count : contexts.size();

            GetProtocolDescriptionResponse result = new GetProtocolDescriptionResponse();
            result.setOffset(offset);
            result.setCountAll(contexts.size());

            for (int i = 0; i < contexts.size(); i++) {
                if (i >= offset && result.getItems().size() < count) {
                    ProtocolDescriptionContextEntry context = contexts.get(i);
                    String bundleId = context.getResourceBundle();
                    if (bundleId == null) {
                        bundleId = "ContentService";
                    }
                    if (language == null) {
                        language = Locale.ENGLISH.getLanguage();
                    }
                    ResourceBundle bundle = ResourceBundle.getBundle(bundleId, new Locale(language));
                    String name = bundle.getString(context.getNameKey());

                    ProtocolDescriptionContext resultContext = new ProtocolDescriptionContext(context.getContextId(), name, getImages(context.getContextId()));
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

    /**
     * Initialize dynamic contexts. This method does nothing by default but can be overridden in
     * sub classes which need to setup contexts or information in the contexts based on calls to the
     * backend service.
     *
     * @param userService The user service related to this request
     */
    protected void initializeDynamicContexts(UserServiceResponse userService, String language) {
        // Do nothing
    }

    protected void initializeDynamicContexts(String language) {
        String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
        DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
        if (device != null) {
            UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
            if (userService != null) {
                initializeDynamicContexts(userService, language);
            }
        }
    }

    @Override
    public GetProtocolDescription2Response getProtocolDescription2(Integer offset, Integer count, String language) {
        BusinessCall businessCall = startBusinessCall("getProtocolDescription2");
        businessCall.addParameter("offset", offset);
        businessCall.addParameter("count", count);
        businessCall.addParameter("language", language);
        try {
            offset = offset != null ? offset : 0;
            count = count != null ? count : contexts.size();

            initializeDynamicContexts(language);

            GetProtocolDescription2Response result = new GetProtocolDescription2Response();
            result.setOffset(offset);
            result.setCountAll(contexts.size());

            for (int i = 0; i < contexts.size(); i++) {
                if (i >= offset && result.getItems().size() < count) {
                    ProtocolDescriptionContextEntry context = contexts.get(i);
                    String bundleId = context.getResourceBundle();
                    if (bundleId == null) {
                        bundleId = "ContentService";
                    }
                    if (language == null) {
                        language = Locale.ENGLISH.getLanguage();
                    }
                    ResourceBundle bundle = ResourceBundle.getBundle(bundleId, new Locale(language));
                    String name = bundle.getString(context.getNameKey());

                    ProtocolDescription2Context resultContext = new ProtocolDescription2Context(context.getContextId(), name, getImages(context.getContextId()));
                    resultContext.setSupportedRequests(context.getSupportedRequests2());
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
    public ContentItem getItem(String contextId, String language, Map<String, String> parameters) {
        BusinessCall businessCall = startBusinessCall("getItem");
        businessCall.addParameters(parameters);
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
            if (device != null) {
                UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                if (userService != null) {
                    initializeDynamicContexts(userService,language);
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
                                    ContentItem result = entry.getHandler().getItemOrCatchErrors(userService, language, parameters);
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
    public StreamingReference getItemStreamingRef(String itemId, GetItemStreamingRefRequest request) {
        BusinessCall businessCall = startBusinessCall("getItemStreamingRef");
        businessCall.addParameter("itemId", itemId);
        if (request.getPreferredFormats() != null) {
            businessCall.addParameter("preferredFormats", StringUtils.join(request.getPreferredFormats(), ","));
        }
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
            if (device != null) {
                UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                if (userService != null) {
                    initializeDynamicContexts(userService,null);
                    for (ContentItemHandlerEntry entry : contentItemHandlers) {
                        Matcher m = getPattern(entry.getPattern()).matcher(itemId);
                        if (m.matches()) {
                            m.reset();
                            if (m.find()) {
                                for (int i = 1; i <= m.groupCount(); i++) {
                                    itemId = m.group(i);
                                }
                            }
                            StreamingReference result = entry.getHandler().getItemStreamingRefOrCatchErrors(userService, itemId, request.getPreferredFormats());
                            businessLogger.logSuccessful(businessCall);
                            return result;
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
    public ContentResponse getNextDynamicPlaylistTracks(GetNextDynamicPlaylistTracksRequest request) {
        BusinessCall businessCall = startBusinessCall("getNextDynamicPlaylistTracks");
        businessCall.addParameter("count", request.getCount());
        businessCall.addParameter("type", request.getSelectionParameters() != null ? request.getSelectionParameters().getType() : null);
        if (request.getSelectionParameters() != null && request.getSelectionParameters().getData() != null) {
            Iterator<String> fieldNames = request.getSelectionParameters().getData().fieldNames();
            while (fieldNames.hasNext()) {
                String field = fieldNames.next();
                businessCall.addParameter(field, request.getSelectionParameters().getData().get(field).asText());
            }
        }

        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            DeviceResponse device = getCoreBackendService().getDeviceById(deviceId);
            if (device != null) {
                UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
                if (userService != null) {
                    if (request.getSelectionParameters().getType() != null) {
                        initializeDynamicContexts(userService,null);
                        DynamicPlaylistHandlerEntry foundHandler = null;
                        Map<String, String> adjustedParameters = new HashMap<String, String>();
                        for (DynamicPlaylistHandlerEntry entry : dynamicPlaylistHandlers) {
                            if (entry.getType().equals(request.getSelectionParameters().getType())) {
                                foundHandler = entry;
                                adjustedParameters.clear();
                                for (ParameterValue param : foundHandler.getParameters()) {
                                    if (!param.isOptional() && (request.getSelectionParameters().getData() == null || !request.getSelectionParameters().getData().has(param.getParameter()) || request.getSelectionParameters().getData().get(param.getParameter()).isNull())) {
                                        foundHandler = null;
                                        break;
                                    } else if (param.getValue() != null && request.getSelectionParameters().getData() != null && request.getSelectionParameters().getData().has(param.getParameter()) && !request.getSelectionParameters().getData().get(param.getParameter()).isNull()) {
                                        Matcher m = getPattern(param.getValue()).matcher(request.getSelectionParameters().getData().get(param.getParameter()).asText());
                                        if (m.matches()) {
                                            m.reset();
                                            if (param.getParameters().size() > 0 && m.find()) {
                                                for (int i = 1; i <= m.groupCount(); i++) {
                                                    String attributeValue = m.group(i);
                                                    adjustedParameters.put(param.getParameters().get(i - 1), attributeValue);
                                                }
                                            } else {
                                                adjustedParameters.put(param.getParameter(), request.getSelectionParameters().getData().get(param.getParameter()).asText());
                                            }
                                        } else if (!param.isOptional()) {
                                            throw new InvalidParameterException(param.getParameter(), request.getSelectionParameters().getData().get(param.getParameter()).asText());
                                        }
                                    } else if (param.getValue() != null && !param.isOptional()) {
                                        foundHandler = null;
                                        break;
                                    } else if (param.getValue() == null) {
                                        adjustedParameters.put(param.getParameter(), request.getSelectionParameters().getData().get(param.getParameter()).asText());
                                    }
                                }
                            }
                            if (foundHandler != null) {
                                break;
                            }
                        }
                        if (foundHandler != null) {

                            ContentResponse result = foundHandler.getHandler().getNextDynamicPlaylistTracks(userService, request.getCount(), adjustedParameters, request.getPreviousItems());
                            result.setCount(result.getItems().size());
                            businessLogger.logSuccessful(businessCall);
                            return result;
                        } else {
                            businessLogger.logSuccessful(businessCall);
                            ContentResponse result = new ContentResponse();
                            result.setCount(0);
                            return result;
                        }
                    } else {
                        businessLogger.logFailed(businessCall, "Missing parameter: selectionParameters.type");
                    }
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
    public Boolean addItem(String contextId, Map<String, String> parameters) {
        BusinessCall businessCall = startBusinessCall("addItem");
        businessCall.addParameters(parameters);
        try {
            String deviceId = InjectHelper.instance(RequestContext.class).getDeviceId();
            UserServiceResponse userService = getCoreBackendService().getUserServiceByDevice(deviceId);
            if (userService != null) {
                initializeDynamicContexts(userService, parameters.get("language"));
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
                initializeDynamicContexts(userService,null);
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
    public ContentResponse findItems(Integer offset, Integer count, String contextId, String language, Map<String, String> parameters) {
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
                    initializeDynamicContexts(userService,language);
                    ContentResponse result = findItems(userService, language, offset, count, contextId, parameters);
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

    protected ContentResponse findItems(UserServiceResponse userService, String language, Integer offset, Integer count, String contextId, Map<String, String> parameters) {
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
            ContentResponse result = foundHandler.getHandler().findItems(userService, language, foundParameters, offset, count);
            if (result.getCount() == null) {
                result.setCount(result.getItems().size());
            }
            return result;
        }
        throw new InvalidParameterException();
    }

    public static interface ContentHandler {
        ContentResponse findItems(UserServiceResponse userService, String language, Map<String, String> parameters, Integer offset, Integer count);
    }

    public static interface DynamicPlaylistHandler {
        ContentResponse getNextDynamicPlaylistTracks(UserServiceResponse userService, Integer count, Map<String, String> parameters, List<ContentItem> previousItems);
    }

    public static interface ContentItemHandler {
        ContentItem getItemOrCatchErrors(UserServiceResponse userService, String language, Map<String, String> parameters);

        StreamingReference getItemStreamingRefOrCatchErrors(UserServiceResponse userService, String trackId, List<String> preferredFormats);
    }

    public static interface ManagementItemHandler {
        Boolean addItemOrCatchErrors(UserServiceResponse userService, String contextId, String itemId);

        Boolean removeItemOrCatchErrors(UserServiceResponse userService, String contextId, String itemId);
    }

    public static abstract class BusinessCallHandler {
        protected abstract CoreBackendService getCoreBackendService();

        protected abstract BusinessCall createBusinessCall(String serviceId, String deviceId, String deviceModel, String userId, String deviceAddress, String method);

        public BusinessCall startBusinessCall(String service, String method) {
            RequestContext requestContext = InjectHelper.instance(RequestContext.class);
            DeviceResponse device = null;
            if (requestContext.getDeviceId() != null) {
                device = getCoreBackendService().getDeviceById(requestContext.getDeviceId());
            }
            return createBusinessCall(service, requestContext.getDeviceId(), device != null ? device.getModel() : null, device != null ? device.getUserId() : requestContext.getUserId(), requestContext.getDeviceAddress(), method);
        }

        public void logSuccessfulBusinessCall(BusinessCall businessCall) {
            InjectHelper.instance(BusinessLogger.class).logSuccessful(businessCall);
        }

        public void logFailedBusinessCall(BusinessCall businessCall, String error) {
            InjectHelper.instance(BusinessLogger.class).logFailed(businessCall, error);
        }

        public void logFailedBusinessCall(BusinessCall businessCall, String error, Throwable exception) {
            InjectHelper.instance(BusinessLogger.class).logFailed(businessCall, error, exception);
        }

        public void logFailedBusinessCall(BusinessCall businessCall, Throwable exception) {
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
        public ContentItem getItemOrCatchErrors(UserServiceResponse userService, String language, Map<String, String> parameters) {
            try {
                return getItem(userService, language, parameters);
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

        @Override
        public StreamingReference getItemStreamingRefOrCatchErrors(UserServiceResponse userService, String itemId, List<String> preferredFormats) {
            try {
                return getItemStreamingRef(userService, itemId, preferredFormats);
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

        protected StreamingReference getItemStreamingRef(UserServiceResponse userService, String itemId, List<String> preferredFormats) throws IOException {
            return null;
        }

        protected abstract ContentItem getItem(UserServiceResponse userService, String language, Map<String, String> parameters) throws IOException;
    }

    public static abstract class AbstractContentHandler extends BusinessCallHandler implements ContentHandler {

        @Override
        public ContentResponse findItems(UserServiceResponse userService, String language, Map<String, String> parameters, Integer offset, Integer count) {
            ContentResponse result = new ContentResponse();
            result.setOffset(offset);

            try {
                result = findItems(userService, language, parameters, offset, count, result);
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

        protected abstract ContentResponse findItems(UserServiceResponse userService, String language, Map<String, String> parameters, Integer offset, Integer count, ContentResponse result) throws IOException;

    }

    public static abstract class AbstractDynamicPlaylistHandler extends BusinessCallHandler implements DynamicPlaylistHandler {

        @Override
        public ContentResponse getNextDynamicPlaylistTracks(UserServiceResponse userService, Integer count, Map<String, String> parameters, List<ContentItem> previousItems) {
            ContentResponse result = new ContentResponse();

            try {
                result = getNextDynamicPlaylistTracks(userService, count, parameters, previousItems, result);
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

        protected abstract ContentResponse getNextDynamicPlaylistTracks(UserServiceResponse userService, Integer count, Map<String, String> parameters, List<ContentItem> previousItems, ContentResponse result) throws IOException;
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

    private class DynamicPlaylistHandlerEntry {
        private DynamicPlaylistHandler handler;
        private String type;
        private Set<ParameterValue> parameters = new TreeSet<ParameterValue>();

        public DynamicPlaylistHandler getHandler() {
            return handler;
        }

        public void setHandler(DynamicPlaylistHandler handler) {
            this.handler = handler;
        }

        private String getType() {
            return type;
        }

        private void setType(String type) {
            this.type = type;
        }

        private Set<ParameterValue> getParameters() {
            return parameters;
        }

        private void setParameters(Set<ParameterValue> parameters) {
            this.parameters = parameters;
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
        private boolean optional;
        private boolean predefined;
        private List<String> parameters = new ArrayList<String>();

        public ParameterValue(String parameter, String value) {
            this(parameter, value, false);
        }

        public ParameterValue(String parameter, String value, boolean optional) {
            this(parameter, value, optional, false);
        }

        public ParameterValue(String parameter, String value, boolean optional, boolean predefined) {
            this.parameter = parameter;
            this.value = value;
            this.optional = optional;
            this.predefined = predefined;
            if (value != null && value.contains("{")) {
                Matcher m = PARAMETER_PATTERN.matcher(value);
                while (m.find()) {
                    parameters.add(m.group(1));
                }
                this.value = PARAMETER_PATTERN.matcher(value).replaceAll("(.*?)");
            }
        }

        public ParameterValue(String parameter) {
            this(parameter, false);
        }

        public ParameterValue(String parameter, boolean optional) {
            this(parameter, null, optional);
        }

        public boolean isOptional() {
            return optional;
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

        public boolean isPredefined() {
            return predefined;
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
