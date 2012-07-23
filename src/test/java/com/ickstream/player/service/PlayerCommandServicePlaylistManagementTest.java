/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.service;

import com.ickstream.player.model.PlayerStatus;
import com.ickstream.player.model.Playlist;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.player.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlayerCommandServicePlaylistManagementTest {

    @Test
    public void testGetPlayerStatusEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().clear();
        status.setPlaylistPos(null);
        status.setSeekPos(null);
        PlayerCommandService service = new PlayerCommandService(status);

        PlayerStatusResponse returnedStatus = service.getPlayerStatus();

        Assert.assertEquals(returnedStatus.getPlaying(), status.getPlaying());
        Assert.assertEquals(returnedStatus.getPlaylistPos(), status.getPlaylistPos());
        Assert.assertEquals(returnedStatus.getSeekPos(), status.getSeekPos());
        Assert.assertNull(returnedStatus.getTrack());
    }

    @Test
    public void testGetPlayerStatusNoPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().clear();
        status.setPlaylistPos(null);
        status.setSeekPos(null);
        status.getPlaylist().setId(null);
        status.getPlaylist().setName(null);
        PlayerCommandService service = new PlayerCommandService(status);

        PlayerStatusResponse returnedStatus = service.getPlayerStatus();

        Assert.assertEquals(returnedStatus.getPlaying(), status.getPlaying());
        Assert.assertEquals(returnedStatus.getPlaylistPos(), status.getPlaylistPos());
        Assert.assertEquals(returnedStatus.getSeekPos(), status.getSeekPos());
        Assert.assertNull(returnedStatus.getTrack());
    }

    @Test
    public void testSetPlaylistName() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        SetPlaylistNameResponse playlist = service.setPlaylistName(new SetPlaylistNameRequest("someid", "somename"));

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(playlist.getCountAll(), new Integer(3));
    }

    @Test
    public void testGetPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        ChunkedRequest sublist = new ChunkedRequest();

        PlaylistResponse playlist = service.getPlaylist(sublist);

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(playlist.getTracks_loop().size(), status.getPlaylist().getItems().size());
        Assert.assertEquals(playlist.getTracks_loop().get(0).getId(), status.getPlaylist().getItems().get(0).getId());
        Assert.assertEquals(playlist.getTracks_loop().get(1).getId(), status.getPlaylist().getItems().get(1).getId());
        Assert.assertEquals(playlist.getTracks_loop().get(2).getId(), status.getPlaylist().getItems().get(2).getId());
    }

    @Test
    public void testGetPlaylistEmpty() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().clear();
        PlayerCommandService service = new PlayerCommandService(status);

        ChunkedRequest sublist = new ChunkedRequest();

        PlaylistResponse playlist = service.getPlaylist(sublist);

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(playlist.getTracks_loop().size(), status.getPlaylist().getItems().size());
        Assert.assertEquals(playlist.getTracks_loop().size(), 0);
    }

    @Test
    public void testGetPlaylistUnknown() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().setId(null);
        status.getPlaylist().setName(null);
        PlayerCommandService service = new PlayerCommandService(status);

        ChunkedRequest sublist = new ChunkedRequest();

        PlaylistResponse playlist = service.getPlaylist(sublist);

        Assert.assertNull(playlist.getPlaylistId());
        Assert.assertNull(playlist.getPlaylistName());
        Assert.assertEquals(playlist.getTracks_loop().size(), status.getPlaylist().getItems().size());
        Assert.assertEquals(playlist.getTracks_loop().get(0).getId(), status.getPlaylist().getItems().get(0).getId());
        Assert.assertEquals(playlist.getTracks_loop().get(1).getId(), status.getPlaylist().getItems().get(1).getId());
        Assert.assertEquals(playlist.getTracks_loop().get(2).getId(), status.getPlaylist().getItems().get(2).getId());
    }

    @Test
    public void testGetPlaylistFromOffset() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        ChunkedRequest sublist = new ChunkedRequest();
        sublist.setOffset(1);

        PlaylistResponse playlist = service.getPlaylist(sublist);

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(playlist.getTracks_loop().size(), status.getPlaylist().getItems().size() - 1);
        Assert.assertEquals(playlist.getTracks_loop().get(0).getId(), status.getPlaylist().getItems().get(1).getId());
        Assert.assertEquals(playlist.getTracks_loop().get(1).getId(), status.getPlaylist().getItems().get(2).getId());
    }

    @Test
    public void testGetPlaylistToCount() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        ChunkedRequest sublist = new ChunkedRequest();
        sublist.setCount(2);

        PlaylistResponse playlist = service.getPlaylist(sublist);

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(playlist.getTracks_loop().size(), status.getPlaylist().getItems().size() - 1);
        Assert.assertEquals(playlist.getTracks_loop().get(0).getId(), status.getPlaylist().getItems().get(0).getId());
        Assert.assertEquals(playlist.getTracks_loop().get(1).getId(), status.getPlaylist().getItems().get(1).getId());
    }

    @Test
    public void testGetPlaylistFromOffsetToCount() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        ChunkedRequest sublist = new ChunkedRequest();
        sublist.setOffset(1);
        sublist.setCount(1);

        PlaylistResponse playlist = service.getPlaylist(sublist);

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(playlist.getTracks_loop().size(), 1);
        Assert.assertEquals(playlist.getTracks_loop().get(0).getId(), status.getPlaylist().getItems().get(1).getId());
    }

    @Test
    public void testAddTracksToEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaylistPos();
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("added1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistAddTracksRequest request = new PlaylistAddTracksRequest();
        request.getTracks_loop().add(added1);
        request.getTracks_loop().add(added2);

        PlaylistModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), oldPos);
        Assert.assertEquals(status.getPlaylist().getItems().size(), 7);
        Assert.assertEquals(status.getPlaylist().getItems().get(5), added1);
        Assert.assertEquals(status.getPlaylist().getItems().get(6), added2);
    }

    @Test
    public void testAddTracksToBeginning() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaylistPos();
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("added1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistAddTracksRequest request = new PlaylistAddTracksRequest();
        request.getTracks_loop().add(added1);
        request.getTracks_loop().add(added2);
        request.setPlaylistPos(0);

        PlaylistModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(oldPos + 2));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 7);
        Assert.assertEquals(status.getPlaylist().getItems().get(0), added1);
        Assert.assertEquals(status.getPlaylist().getItems().get(1), added2);
    }

    @Test
    public void testAddTracksToMiddleBeforeCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaylistPos();
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("added1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistAddTracksRequest request = new PlaylistAddTracksRequest();
        request.getTracks_loop().add(added1);
        request.getTracks_loop().add(added2);
        request.setPlaylistPos(1);

        PlaylistModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(oldPos + 2));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 7);
        Assert.assertEquals(status.getPlaylist().getItems().get(1), added1);
        Assert.assertEquals(status.getPlaylist().getItems().get(2), added2);
    }

    @Test
    public void testAddTracksToMiddleAfterCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaylistPos();
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("added1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistAddTracksRequest request = new PlaylistAddTracksRequest();
        request.getTracks_loop().add(added1);
        request.getTracks_loop().add(added2);
        request.setPlaylistPos(2);

        PlaylistModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), oldPos);
        Assert.assertEquals(status.getPlaylist().getItems().size(), 7);
        Assert.assertEquals(status.getPlaylist().getItems().get(2), added1);
        Assert.assertEquals(status.getPlaylist().getItems().get(3), added2);
    }

    @Test
    public void testRemoveTracksFromEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaylistPos();
        PlaylistItemReference removed1 = new PlaylistItemReference();
        removed1.setId("track5");
        removed1.setPlaylistPos(4);
        PlaylistItemReference removed2 = new PlaylistItemReference();
        removed2.setId("track4");
        removed2.setPlaylistPos(3);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistRemoveTracksRequest request = new PlaylistRemoveTracksRequest();
        request.getTracks_loop().add(removed1);
        request.getTracks_loop().add(removed2);

        PlaylistModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), oldPos);
        Assert.assertEquals(status.getPlaylist().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromBeginning() {
        PlayerStatus status = getDefaultPlayerStatus(5);

        PlaylistItemReference removed1 = new PlaylistItemReference();
        removed1.setId("track2");
        removed1.setPlaylistPos(1);
        PlaylistItemReference removed2 = new PlaylistItemReference();
        removed2.setId("track1");
        removed2.setPlaylistPos(0);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistRemoveTracksRequest request = new PlaylistRemoveTracksRequest();
        request.getTracks_loop().add(removed1);
        request.getTracks_loop().add(removed2);

        PlaylistModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromMiddleBeforeAndAfterCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaylistItemReference removed1 = new PlaylistItemReference();
        removed1.setId("track1");
        removed1.setPlaylistPos(0);
        PlaylistItemReference removed2 = new PlaylistItemReference();
        removed2.setId("track3");
        removed2.setPlaylistPos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistRemoveTracksRequest request = new PlaylistRemoveTracksRequest();
        request.getTracks_loop().add(removed1);
        request.getTracks_loop().add(removed2);

        PlaylistModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksWithoutPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaylistItem duplicateItem = new PlaylistItem();
        duplicateItem.setId("track1");
        status.getPlaylist().getItems().add(duplicateItem);

        PlaylistItemReference removed1 = new PlaylistItemReference();
        removed1.setId("track1");
        PlaylistItemReference removed2 = new PlaylistItemReference();
        removed2.setId("track3");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistRemoveTracksRequest request = new PlaylistRemoveTracksRequest();
        request.getTracks_loop().add(removed1);
        request.getTracks_loop().add(removed2);

        PlaylistModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksInvalidPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);

        PlaylistItemReference removed1 = new PlaylistItemReference();
        removed1.setId("track2");
        removed1.setPlaylistPos(0);
        PlaylistItemReference removed2 = new PlaylistItemReference();
        removed2.setId("track1");
        removed2.setPlaylistPos(0);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistRemoveTracksRequest request = new PlaylistRemoveTracksRequest();
        request.getTracks_loop().add(removed1);
        request.getTracks_loop().add(removed2);

        try {
            PlaylistModificationResponse response = service.removeTracks(request);
            Assert.assertTrue(false, "Error was not detected");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testSetTracksWithPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("mytrack1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("mytrack2");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistSetTracksRequest request = new PlaylistSetTracksRequest();
        request.setPlaylistId("newplaylist1");
        request.setPlaylistId("NewPlaylist1");
        request.getTracks_loop().add(added1);
        request.getTracks_loop().add(added2);

        PlaylistModificationResponse response = service.setTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 2);
        Assert.assertEquals(status.getPlaylist().getItems().get(0), added1);
        Assert.assertEquals(status.getPlaylist().getItems().get(1), added2);
        Assert.assertEquals(status.getPlaylist().getId(), request.getPlaylistId());
        Assert.assertEquals(status.getPlaylist().getName(), request.getPlaylistName());
    }

    @Test
    public void testSetTracksWithoutPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("mytrack1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("mytrack2");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistSetTracksRequest request = new PlaylistSetTracksRequest();
        request.getTracks_loop().add(added1);
        request.getTracks_loop().add(added2);

        PlaylistModificationResponse response = service.setTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(status.getPlaylist().getItems().size(), 2);
        Assert.assertEquals(status.getPlaylist().getItems().get(0), added1);
        Assert.assertEquals(status.getPlaylist().getItems().get(1), added2);
        Assert.assertNull(status.getPlaylist().getId());
        Assert.assertNull(status.getPlaylist().getName());
    }

    @Test
    public void testSetTracksEmpty() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaylistItem added1 = new PlaylistItem();
        added1.setId("mytrack1");
        PlaylistItem added2 = new PlaylistItem();
        added2.setId("mytrack2");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaylistSetTracksRequest request = new PlaylistSetTracksRequest();
        request.setPlaylistId("newplaylist1");
        request.setPlaylistId("NewPlaylist1");

        PlaylistModificationResponse response = service.setTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertNull(response.getPlaylistPos());
        Assert.assertNull(status.getSeekPos());
        Assert.assertNull(status.getPlaylistPos());
        Assert.assertEquals(status.getPlaylist().getItems().size(), 0);
        Assert.assertEquals(status.getPlaylist().getId(), request.getPlaylistId());
        Assert.assertEquals(status.getPlaylist().getName(), request.getPlaylistName());
    }

    private Playlist getDefaultPlaylist(int numOfTracks) {
        Playlist playlist = new Playlist();
        playlist.setId("playlist1");
        playlist.setName("Playlist1Name");
        for (int i = 0; i < numOfTracks; i++) {
            PlaylistItem item = new PlaylistItem();
            item.setId("track" + (i + 1));
            playlist.getItems().add(item);
        }
        return playlist;
    }

    private PlayerStatus getDefaultPlayerStatus(int numOfTracks) {
        PlayerStatus status = new PlayerStatus();
        status.setPlaying(true);
        status.setSeekPos(42.3);
        status.setPlaylist(getDefaultPlaylist(numOfTracks));
        status.setPlaylistPos(1);
        return status;
    }
}
