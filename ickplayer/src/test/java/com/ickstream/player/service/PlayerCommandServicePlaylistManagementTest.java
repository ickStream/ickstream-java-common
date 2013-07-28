/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.service;

import com.ickstream.player.model.PlaybackQueue;
import com.ickstream.player.model.PlayerStatus;
import com.ickstream.protocol.service.player.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class PlayerCommandServicePlaylistManagementTest {

    @Test
    public void testGetPlayerStatusEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().clear();
        status.setPlaybackQueuePos(null);
        status.setSeekPos(null);
        PlayerCommandService service = new PlayerCommandService(status);

        PlayerStatusResponse returnedStatus = service.getPlayerStatus();

        Assert.assertEquals(returnedStatus.getPlaying(), status.getPlaying());
        Assert.assertEquals(returnedStatus.getPlaybackQueuePos(), status.getPlaybackQueuePos());
        Assert.assertEquals(returnedStatus.getSeekPos(), status.getSeekPos());
        Assert.assertNull(returnedStatus.getTrack());
    }

    @Test
    public void testGetPlayerStatusNoPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().clear();
        status.setPlaybackQueuePos(null);
        status.setSeekPos(null);
        status.getPlaybackQueue().setId(null);
        status.getPlaybackQueue().setName(null);
        PlayerCommandService service = new PlayerCommandService(status);

        PlayerStatusResponse returnedStatus = service.getPlayerStatus();

        Assert.assertEquals(returnedStatus.getPlaying(), status.getPlaying());
        Assert.assertEquals(returnedStatus.getPlaybackQueuePos(), status.getPlaybackQueuePos());
        Assert.assertEquals(returnedStatus.getSeekPos(), status.getSeekPos());
        Assert.assertNull(returnedStatus.getTrack());
    }

    @Test
    public void testSetPlaylistName() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        SetPlaylistNameResponse playlist = service.setPlaylistName(new SetPlaylistNameRequest("someid", "somename"));

        Assert.assertEquals(playlist.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playlist.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playlist.getCountAll(), new Integer(3));
    }

    @Test
    public void testGetPlaybackQueue() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size());
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getItems().get(0).getId());
        Assert.assertEquals(playbackQueue.getItems().get(1).getId(), status.getPlaybackQueue().getItems().get(1).getId());
        Assert.assertEquals(playbackQueue.getItems().get(2).getId(), status.getPlaybackQueue().getItems().get(2).getId());
    }

    @Test
    public void testGetPlaybackQueueEmpty() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().clear();
        PlayerCommandService service = new PlayerCommandService(status);

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size());
        Assert.assertEquals(playbackQueue.getItems().size(), 0);
    }

    @Test
    public void testGetPlaybackQueueUnknown() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().setId(null);
        status.getPlaybackQueue().setName(null);
        PlayerCommandService service = new PlayerCommandService(status);

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertNull(playbackQueue.getPlaylistId());
        Assert.assertNull(playbackQueue.getPlaylistName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size());
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getItems().get(0).getId());
        Assert.assertEquals(playbackQueue.getItems().get(1).getId(), status.getPlaybackQueue().getItems().get(1).getId());
        Assert.assertEquals(playbackQueue.getItems().get(2).getId(), status.getPlaybackQueue().getItems().get(2).getId());
    }

    @Test
    public void testGetPlaybackQueueFromOffset() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();
        sublist.setOffset(1);

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size() - 1);
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getItems().get(1).getId());
        Assert.assertEquals(playbackQueue.getItems().get(1).getId(), status.getPlaybackQueue().getItems().get(2).getId());
    }

    @Test
    public void testGetPlaybackQueueToCount() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();
        sublist.setCount(2);

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size() - 1);
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getItems().get(0).getId());
        Assert.assertEquals(playbackQueue.getItems().get(1).getId(), status.getPlaybackQueue().getItems().get(1).getId());
    }

    @Test
    public void testGetPlaybackQueueFromOffsetToCount() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();
        sublist.setOffset(1);
        sublist.setCount(1);

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), 1);
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getItems().get(1).getId());
    }

    @Test
    public void testAddTracksToEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaybackQueuePos();
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("added1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueAddTracksRequest request = new PlaybackQueueAddTracksRequest();
        request.getItems().add(added1);
        request.getItems().add(added2);

        PlaybackQueueModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), oldPos);
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(5), added1);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(6), added2);
    }

    @Test
    public void testAddTracksToBeginning() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaybackQueuePos();
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("added1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueAddTracksRequest request = new PlaybackQueueAddTracksRequest();
        request.getItems().add(added1);
        request.getItems().add(added2);
        request.setPlaybackQueuePos(0);

        PlaybackQueueModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(oldPos + 2));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1), added2);
    }

    @Test
    public void testAddTracksToMiddleBeforeCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaybackQueuePos();
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("added1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueAddTracksRequest request = new PlaybackQueueAddTracksRequest();
        request.getItems().add(added1);
        request.getItems().add(added2);
        request.setPlaybackQueuePos(1);

        PlaybackQueueModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(oldPos + 2));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1), added1);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2), added2);
    }

    @Test
    public void testAddTracksToMiddleAfterCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaybackQueuePos();
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("added1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("added1");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueAddTracksRequest request = new PlaybackQueueAddTracksRequest();
        request.getItems().add(added1);
        request.getItems().add(added2);
        request.setPlaybackQueuePos(2);

        PlaybackQueueModificationResponse response = service.addTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), oldPos);
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2), added1);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3), added2);
    }

    @Test
    public void testRemoveTracksFromEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        Integer oldPos = status.getPlaybackQueuePos();
        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track5");
        removed1.setPlaybackQueuePos(4);
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track4");
        removed2.setPlaybackQueuePos(3);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);

        PlaybackQueueModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), oldPos);
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromEndCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(4);
        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track5");
        removed1.setPlaybackQueuePos(4);
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track4");
        removed2.setPlaybackQueuePos(3);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);

        PlaybackQueueModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromEndCurrentLast() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(4);
        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track5");
        removed1.setPlaybackQueuePos(4);
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track4");
        removed2.setPlaybackQueuePos(3);
        PlaybackQueueItemReference removed3 = new PlaybackQueueItemReference();
        removed3.setId("track3");
        removed3.setPlaybackQueuePos(2);
        PlaybackQueueItemReference removed4 = new PlaybackQueueItemReference();
        removed4.setId("track2");
        removed4.setPlaybackQueuePos(1);
        PlaybackQueueItemReference removed5 = new PlaybackQueueItemReference();
        removed5.setId("track1");
        removed5.setPlaybackQueuePos(0);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);
        request.getItems().add(removed3);
        request.getItems().add(removed4);
        request.getItems().add(removed5);

        PlaybackQueueModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 0);
    }

    @Test
    public void testRemoveTracksFromBeginning() {
        PlayerStatus status = getDefaultPlayerStatus(5);

        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track2");
        removed1.setPlaybackQueuePos(1);
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track1");
        removed2.setPlaybackQueuePos(0);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);

        PlaybackQueueModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromMiddleBeforeAndAfterCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track1");
        removed1.setPlaybackQueuePos(0);
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track3");
        removed2.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);

        PlaybackQueueModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksWithoutPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaybackQueueItem duplicateItem = new PlaybackQueueItem();
        duplicateItem.setId("track1");
        status.getPlaybackQueue().getItems().add(duplicateItem);

        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track1");
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track3");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);

        PlaybackQueueModificationResponse response = service.removeTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 3);
    }

    @Test
    public void testRemoveTracksInvalidPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);

        PlaybackQueueItemReference removed1 = new PlaybackQueueItemReference();
        removed1.setId("track2");
        removed1.setPlaybackQueuePos(0);
        PlaybackQueueItemReference removed2 = new PlaybackQueueItemReference();
        removed2.setId("track1");
        removed2.setPlaybackQueuePos(0);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueRemoveTracksRequest request = new PlaybackQueueRemoveTracksRequest();
        request.getItems().add(removed1);
        request.getItems().add(removed2);

        try {
            PlaybackQueueModificationResponse response = service.removeTracks(request);
            Assert.assertTrue(false, "Error was not detected");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testSetTracksWithPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("mytrack1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("mytrack2");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueSetTracksRequest request = new PlaybackQueueSetTracksRequest();
        request.setPlaylistId("newplaylist1");
        request.setPlaylistId("NewPlaylist1");
        request.getItems().add(added1);
        request.getItems().add(added2);

        PlaybackQueueModificationResponse response = service.setTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 2);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1), added2);
        Assert.assertEquals(status.getPlaybackQueue().getId(), request.getPlaylistId());
        Assert.assertEquals(status.getPlaybackQueue().getName(), request.getPlaylistName());
    }

    @Test
    public void testSetTracksWithoutPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("mytrack1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("mytrack2");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueSetTracksRequest request = new PlaybackQueueSetTracksRequest();
        request.getItems().add(added1);
        request.getItems().add(added2);

        PlaybackQueueModificationResponse response = service.setTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 2);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1), added2);
        Assert.assertNull(status.getPlaybackQueue().getId());
        Assert.assertNull(status.getPlaybackQueue().getName());
    }

    @Test
    public void testSetTracksEmpty() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaybackQueueItem added1 = new PlaybackQueueItem();
        added1.setId("mytrack1");
        PlaybackQueueItem added2 = new PlaybackQueueItem();
        added2.setId("mytrack2");
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueSetTracksRequest request = new PlaybackQueueSetTracksRequest();
        request.setPlaylistId("newplaylist1");
        request.setPlaylistId("NewPlaylist1");

        PlaybackQueueModificationResponse response = service.setTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertNull(response.getPlaybackQueuePos());
        Assert.assertNull(status.getSeekPos());
        Assert.assertNull(status.getPlaybackQueuePos());
        Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 0);
        Assert.assertEquals(status.getPlaybackQueue().getId(), request.getPlaylistId());
        Assert.assertEquals(status.getPlaybackQueue().getName(), request.getPlaylistName());
    }

    @Test
    public void testMoveTracksInvalidId() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(2);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track2", 0)
        );
        request.setItems(movedTracks);

        try {
            PlaybackQueueModificationResponse response = service.moveTracks(request);
            Assert.assertTrue(false);
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testMoveTracksAllBeforeCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(2);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track1", 0)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksAllAfterCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(4);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track5", 4)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveTracksAllAfterCurrentPosToEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(null);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track4", 3)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(2));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveTracksBeforeToAfterCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(4);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track1", 0)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksBeforeToAfterCurrentPosToEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(null);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track1", 0)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track1");
    }

    @Test
    public void testMoveTracksAfterToBeforeCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(1);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track5", 4)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(3));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(3));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveTracksCurrentToBeforeCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(1);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track3", 2)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksCurrentToAfterCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(4);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track3", 2)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(3));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(3));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksCurrentToEndSpecific() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(5);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track3", 2)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(4));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(4));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track3");
    }

    @Test
    public void testMoveTracksCurrentToEnd() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(null);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track3", 2)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(4));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(4));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track3");
    }

    @Test
    public void testMoveMultipleTracksBeforeToAfterCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(3);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track2", 1),
                new PlaybackQueueItemReference("track1", 0)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(0));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(0));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveMultipleTracksAfterToBeforeCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(1);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track4", 3),
                new PlaybackQueueItemReference("track5", 4)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(4));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(4));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track3");
    }

    @Test
    public void testMoveMultipleTracksBothBeforeAndAfterToBeforeCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(2);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track1", 0),
                new PlaybackQueueItemReference("track5", 4)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(3));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(3));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveMultipleTracksBothBeforeAndAfterToAfterCurrentPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueuePos(2);
        PlayerCommandService service = new PlayerCommandService(status);
        PlaybackQueueMoveTracksRequest request = new PlaybackQueueMoveTracksRequest();
        request.setPlaybackQueuePos(3);
        List<PlaybackQueueItemReference> movedTracks = Arrays.asList(
                new PlaybackQueueItemReference("track1", 0),
                new PlaybackQueueItemReference("track5", 4)
        );
        request.setItems(movedTracks);

        PlaybackQueueModificationResponse response = service.moveTracks(request);

        Assert.assertEquals(response.getResult(), Boolean.TRUE);
        Assert.assertEquals(response.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(1));
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track4");
    }

    private PlaybackQueue getDefaultPlaylist(int numOfTracks) {
        PlaybackQueue playbackQueue = new PlaybackQueue();
        playbackQueue.setId("playlist1");
        playbackQueue.setName("Playlist1Name");
        for (int i = 0; i < numOfTracks; i++) {
            PlaybackQueueItem item = new PlaybackQueueItem();
            item.setId("track" + (i + 1));
            playbackQueue.getItems().add(item);
        }
        return playbackQueue;
    }

    private PlayerStatus getDefaultPlayerStatus(int numOfTracks) {
        PlayerStatus status = new PlayerStatus(new PlaybackQueue());
        status.setPlaying(true);
        status.setSeekPos(42.3);
        status.setPlaybackQueue(getDefaultPlaylist(numOfTracks));
        status.setPlaybackQueuePos(1);
        return status;
    }
}
