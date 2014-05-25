/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.common.jsonrpc.JsonHelper;
import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.content.ContentResponse;
import com.ickstream.protocol.service.content.GetProtocolDescriptionRequest;
import com.ickstream.protocol.service.content.GetProtocolDescriptionResponse;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class FullBrowseMenuTest {
    String ARTIST_TRACK_SERVICE = "" +
            "{" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"contextId\": \"myMusic\",\n" +
            "            \"name\": \"My Music\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"artist\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"track\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"artistId\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }" +
            "    ]" +
            "}";

    String ARTIST_TRACK_WITHOUT_FILTERING_SERVICE = "" +
            "{" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"contextId\": \"myMusic\",\n" +
            "            \"name\": \"My Music\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"artist\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"track\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }," +
            "        {\n" +
            "            \"contextId\": \"allMusic\",\n" +
            "            \"name\": \"All Music\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"track\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\",\"artistId\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }" +
            "    ]" +
            "}";

    String ARTIST_ALBUM_TRACK_WITHOUT_ARTIST_ALBUM_FILTERING_SERVICE = "" +
            "{" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"contextId\": \"myMusic\",\n" +
            "            \"name\": \"My Music\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"artist\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"album\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"artistId\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"track\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"artistId\"],\n" +
            "                        [\"contextId\",\"type\",\"albumId\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }" +
            "    ]" +
            "}";

    String ARTIST_ALBUM_TRACK_SERVICE = "" +
            "{" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"contextId\": \"myMusic\",\n" +
            "            \"name\": \"My Music\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"artist\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"album\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"artistId\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"track\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"artistId\"],\n" +
            "                        [\"contextId\",\"type\",\"artistId\",\"albumId\"],\n" +
            "                        [\"contextId\",\"type\",\"albumId\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }" +
            "    ]" +
            "}";

    String MENU_STREAM_SERVICE = "" +
            "{" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"contextId\": \"allRadio\",\n" +
            "            \"name\": \"Local Radio\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"menu\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"menuId\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"stream\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\",\"menuId\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"menuId\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }" +
            "    ]" +
            "}";

    String ARTIST_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"artist1\"," +
            "            \"type\": \"artist\"" +
            "        }," +
            "        {" +
            "            \"id\": \"artist2\"," +
            "            \"type\": \"artist\"" +
            "}" +
            "   ]" +
            "}";

    String ALBUM_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"album1\"," +
            "            \"type\": \"album\"" +
            "        }," +
            "        {" +
            "            \"id\": \"album2\"," +
            "            \"type\": \"album\"" +
            "        }" +
            "   ]" +
            "}";

    String TRACK_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"track1\"," +
            "            \"type\": \"track\"" +
            "        }," +
            "        {" +
            "            \"id\": \"track2\"," +
            "            \"type\": \"track\"" +
            "        }" +
            "   ]" +
            "}";

    String CATEGORY_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"category1\"," +
            "            \"type\": \"category\"" +
            "        }," +
            "        {" +
            "            \"id\": \"category2\"," +
            "            \"type\": \"category\"" +
            "        }" +
            "   ]" +
            "}";

    String MENU_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"menu1\"," +
            "            \"type\": \"menu\"" +
            "        }," +
            "        {" +
            "            \"id\": \"menu2\"," +
            "            \"type\": \"menu\"" +
            "        }" +
            "   ]" +
            "}";

    String SUB_MENU_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"submenu1\"," +
            "            \"type\": \"menu\"" +
            "        }," +
            "        {" +
            "            \"id\": \"submenu2\"," +
            "            \"type\": \"menu\"" +
            "        }" +
            "   ]" +
            "}";

    String STREAM_LIST = "" +
            "{" +
            "   \"count\": 2," +
            "   \"offset\": 0," +
            "   \"countAll\": 2," +
            "   \"items\": [" +
            "        {" +
            "            \"id\": \"stream1\"," +
            "            \"type\": \"stream\"" +
            "        }," +
            "        {" +
            "            \"id\": \"stream2\"," +
            "            \"type\": \"stream\"" +
            "        }" +
            "   ]" +
            "}";

    String CATEGORY_CATEGORY_STREAM_SERVICE = "" +
            "{" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"contextId\": \"allRadio\",\n" +
            "            \"name\": \"Internet Radio\",\n" +
            "            \"supportedRequests\": [\n" +
            "                {\n" +
            "                    \"type\": \"category\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\"],\n" +
            "                        [\"contextId\",\"type\",\"categoryId\"]\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"type\": \"stream\",\n" +
            "                    \"parameters\": [\n" +
            "                        [\"contextId\",\"type\",\"categoryId\"]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }" +
            "    ]" +
            "}";

    private class MenuItemImpl extends ContentMenuItem {

        public MenuItemImpl(String id, String type) {
            this(id, type, null);
        }

        public MenuItemImpl(String id, String type, MenuItem parent) {
            super(null, null, new ContentItem(), parent);
            getContentItem().setId(id);
            getContentItem().setType(type);
        }

        public MenuItemImpl(String id, String type, List<String> preferredChildItems, MenuItem parent) {
            super(null, null, new ContentItem(), parent);
            getContentItem().setId(id);
            getContentItem().setType(type);
            getContentItem().setPreferredChildItems(preferredChildItems);
        }
    }

    private class TypeMenuItemImpl extends TypeMenuItem {

        public TypeMenuItemImpl(String type) {
            super(null, null, type, null, null, null);
        }

        public TypeMenuItemImpl(String type, MenuItem parent) {
            super(null, null, type, null, null, parent);
        }

    }

    JsonHelper jsonHelper = new JsonHelper();

    private BrowseMenu createBrowseMenu(ServiceController serviceController) {
        BrowseMenu menu = new FullBrowseMenu(serviceController, Arrays.asList(
                new TypeMenuItem("category", "Categories", null),
                new TypeMenuItem("artist", "Artists", null),
                new TypeMenuItem("album", "Albums", null),
                new TypeMenuItem("track", "Songs", null),
                new TypeMenuItem("stream", "Streams", null)
        ));
        return menu;
    }

    private void setupGetProtocolDescriptionAnswer(ServiceController serviceController, final GetProtocolDescriptionResponse protocolDescription) {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                MessageHandler<GetProtocolDescriptionResponse> callback = (MessageHandler<GetProtocolDescriptionResponse>) args[1];
                callback.onMessage(protocolDescription);
                return null;
            }
        }).when(serviceController).getProtocolDescription(Mockito.any(GetProtocolDescriptionRequest.class), Mockito.any(MessageHandler.class), Mockito.any(Integer.class));
    }

    private void setupFindItemsResponse(ServiceController serviceController, String contextId, String language, Map<String, Object> parameters, final ContentResponse response) {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                MessageHandler<ContentResponse> callback = (MessageHandler<ContentResponse>) args[4];
                callback.onMessage(response);
                return null;
            }
        }).when(serviceController).findItems(Mockito.any(ChunkedRequest.class), Mockito.eq(contextId), Mockito.eq(language), Mockito.eq(parameters), Mockito.any(MessageHandler.class), Mockito.any(Integer.class));
    }

    @Test
    public void testArtistTrack_TopLevelContexts() throws ServiceTimeoutException, ServiceException {
        ServiceController contentService = Mockito.mock(ServiceController.class);
        final GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Should not call findItems");
            }
        }).when(contentService).findItems(Mockito.any(ChunkedRequest.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class), Mockito.any(MessageHandler.class));

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findContexts(Locale.ENGLISH.getLanguage(), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 1);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContextMenuItem);
    }

    @Test
    public void testArtistTrack_Artists() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "artist");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse artistList = jsonHelper.stringToObject(ARTIST_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, artistList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new TypeMenuItemImpl("artist"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistTrack_ArtistTracks() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "track");
        expectedRequest.put("artistId", "artist1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(TRACK_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new MenuItemImpl("artist1", "artist"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistTrackWithoutFiltering_TopLevelContexts() throws ServiceTimeoutException, ServiceException {
        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_TRACK_WITHOUT_FILTERING_SERVICE, GetProtocolDescriptionResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Should not call findItems");
            }
        }).when(contentService).findItems(Mockito.any(ChunkedRequest.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class), Mockito.any(MessageHandler.class));

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findContexts(Locale.ENGLISH.getLanguage(), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(1, response[0].getItems().size());
        Assert.assertEquals(response[0].getItems().size(), 1);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContextMenuItem);
    }

    @Test
    public void testArtistTrackWithoutFiltering_Artists() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "artist");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_TRACK_WITHOUT_FILTERING_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse artistList = jsonHelper.stringToObject(ARTIST_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, artistList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new TypeMenuItemImpl("artist"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistTrackWithoutFiltering_ArtistTracks() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "track");
        expectedRequest.put("artistId", "artist1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_TRACK_WITHOUT_FILTERING_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(TRACK_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allMusic", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new MenuItemImpl("artist1", "artist"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistAlbumTrack_TopLevelContexts() throws ServiceTimeoutException, ServiceException {
        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_ALBUM_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Should not call findItems");
            }
        }).when(contentService).findItems(Mockito.any(ChunkedRequest.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class), Mockito.any(MessageHandler.class));

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findContexts(Locale.ENGLISH.getLanguage(), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 1);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContextMenuItem);
    }

    @Test
    public void testArtistAlbumTrack_Artists() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "artist");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_ALBUM_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse artistList = jsonHelper.stringToObject(ARTIST_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, artistList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new TypeMenuItemImpl("artist"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistAlbumTrack_ArtistAlbums() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "album");
        expectedRequest.put("artistId", "artist1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_ALBUM_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse albumList = jsonHelper.stringToObject(ALBUM_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, albumList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new MenuItemImpl("artist1", "artist"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 3);
        Assert.assertTrue(response[0].getItems().get(0) instanceof TypeMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(2) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistAlbumTrack_AlbumTracks() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "track");
        expectedRequest.put("albumId", "album1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_ALBUM_TRACK_WITHOUT_ARTIST_ALBUM_FILTERING_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(TRACK_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new MenuItemImpl("album1", "album", new MenuItemImpl("artist1", "artist")), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistAlbumTrack_ArtistAlbumTracks() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "track");
        expectedRequest.put("artistId", "artist1");
        expectedRequest.put("albumId", "album1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_ALBUM_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(TRACK_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new MenuItemImpl("album1", "album", new MenuItemImpl("artist1", "artist")), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testArtistAlbumTrack_ArtistTracks() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "track");
        expectedRequest.put("artistId", "artist1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(ARTIST_ALBUM_TRACK_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(TRACK_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "myMusic", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("myMusic", Locale.ENGLISH.getLanguage(), new TypeMenuItemImpl("track", new MenuItemImpl("artist1", "artist")), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testMenuStream_TopLevelContexts() throws ServiceTimeoutException, ServiceException {
        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(MENU_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Should not call findItems");
            }
        }).when(contentService).findItems(Mockito.any(ChunkedRequest.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class), Mockito.any(MessageHandler.class));

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findContexts(Locale.ENGLISH.getLanguage(), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 1);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContextMenuItem);
    }

    @Test
    public void testMenuStream_Menus() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "menu");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(MENU_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse menuList = jsonHelper.stringToObject(MENU_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allRadio", Locale.ENGLISH.getLanguage(), expectedRequest, menuList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("allRadio", Locale.ENGLISH.getLanguage(), new TypeMenuItemImpl("menu"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testMenuStream_SubMenus() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("menuId", "menu1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(MENU_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse subMenuList = jsonHelper.stringToObject(SUB_MENU_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allRadio", Locale.ENGLISH.getLanguage(), expectedRequest, subMenuList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("allRadio", Locale.ENGLISH.getLanguage(), new MenuItemImpl("menu1", "menu"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }


    @Test
    public void testMenuStream_SubMenuStreams() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("menuId", "submenu1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(MENU_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(STREAM_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allRadio", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("allRadio", Locale.ENGLISH.getLanguage(), new MenuItemImpl("submenu1", "menu", new MenuItemImpl("menu1", "menu")), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testCategoryCategoryStream_Categories() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "category");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(CATEGORY_CATEGORY_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(CATEGORY_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allRadio", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("allRadio", Locale.ENGLISH.getLanguage(), new TypeMenuItemImpl("category"), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }

    @Test
    public void testCategoryCategoryStream_SubCategories() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "category");
        expectedRequest.put("categoryId", "category1");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(CATEGORY_CATEGORY_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(CATEGORY_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allRadio", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("allRadio", Locale.ENGLISH.getLanguage(), new MenuItemImpl("category1", "category", Arrays.asList("category"), null), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 3);
        Assert.assertTrue(response[0].getItems().get(0) instanceof TypeMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(2) instanceof ContentMenuItem);
    }

    @Test
    public void testCategoryCategoryStream_Stream() throws ServiceTimeoutException, ServiceException {
        Map<String, Object> expectedRequest = new HashMap<String, Object>();
        expectedRequest.put("type", "stream");
        expectedRequest.put("categoryId", "category2");

        ServiceController contentService = Mockito.mock(ServiceController.class);
        GetProtocolDescriptionResponse protocolDescription = jsonHelper.stringToObject(CATEGORY_CATEGORY_STREAM_SERVICE, GetProtocolDescriptionResponse.class);
        ContentResponse trackList = jsonHelper.stringToObject(TRACK_LIST, ContentResponse.class);
        setupGetProtocolDescriptionAnswer(contentService, protocolDescription);
        setupFindItemsResponse(contentService, "allRadio", Locale.ENGLISH.getLanguage(), expectedRequest, trackList);

        final BrowseResponse[] response = {null};
        createBrowseMenu(contentService).findItemsInContext("allRadio", Locale.ENGLISH.getLanguage(), new MenuItemImpl("category2", "category", Arrays.asList("stream"), new MenuItemImpl("category1", "category", Arrays.asList("category"), null)), new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse contentResponse) {
                response[0] = contentResponse;
            }
        });
        Assert.assertNotNull(response[0]);
        Assert.assertEquals(response[0].getItems().size(), 2);
        Assert.assertTrue(response[0].getItems().get(0) instanceof ContentMenuItem);
        Assert.assertTrue(response[0].getItems().get(1) instanceof ContentMenuItem);
    }
}
