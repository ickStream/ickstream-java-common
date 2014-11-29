/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.player.service;

import com.ickstream.player.model.PlaybackQueue;
import com.ickstream.player.model.PlaybackQueueItemInstance;
import com.ickstream.player.model.PlayerStatus;
import com.ickstream.protocol.service.player.*;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
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
    public void testGetPlaybackQueueCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
        PlayerCommandService service = new PlayerCommandService(status);

        service.moveTracks(new PlaybackQueueMoveTracksRequest(2, Arrays.asList(new PlaybackQueueItemReference("track1", 0))));

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();
        sublist.setOrder(PlaybackQueueOrder.CURRENT);

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.CURRENT);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size());
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId());
        Assert.assertEquals(playbackQueue.getItems().get(1).getId(), status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId());
        Assert.assertEquals(playbackQueue.getItems().get(2).getId(), status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId());
    }

    @Test
    public void testGetPlaybackQueueOriginal() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
        PlayerCommandService service = new PlayerCommandService(status);

        service.moveTracks(new PlaybackQueueMoveTracksRequest(2, Arrays.asList(new PlaybackQueueItemReference("track1", 0))));

        PlaybackQueueRequest sublist = new PlaybackQueueRequest();
        sublist.setOrder(PlaybackQueueOrder.ORIGINAL);

        PlaybackQueueResponse playbackQueue = service.getPlaybackQueue(sublist);

        Assert.assertEquals(playbackQueue.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(playbackQueue.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(playbackQueue.getOrder(), PlaybackQueueOrder.ORIGINAL);
        Assert.assertEquals(playbackQueue.getItems().size(), status.getPlaybackQueue().getItems().size());
        Assert.assertEquals(playbackQueue.getItems().get(0).getId(), status.getPlaybackQueue().getItems().get(1).getId());
        Assert.assertEquals(playbackQueue.getItems().get(1).getId(), status.getPlaybackQueue().getItems().get(0).getId());
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(5), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(6), added2);
    }

    @Test
    public void testAddTracksToEndShuffleActive() {
        boolean scenario1 = false;
        boolean scenario2 = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(5), added1);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(6), added2);
            Assert.assertEquals(status.getPlaybackQueue().getItems().size(), 7);
            if (!status.getPlaybackQueue().getItems().get(5).equals(added1)) {
                scenario1 = true;
            } else {
                scenario2 = true;
            }
        }
        Assert.assertTrue(scenario1);
        Assert.assertTrue(scenario2);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added2);
    }

    @Test
    public void testAddTracksToBeginningShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added2);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2), added2);
    }

    @Test
    public void testAddTracksToMiddleBeforeCurrentShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2), added2);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3), added2);
    }

    @Test
    public void testAddTracksToMiddleAfterCurrentShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 7);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3), added2);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromEndShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(0)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(1)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(2)));
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromEndCurrentShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(0)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(1)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(2)));
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 0);
    }

    @Test
    public void testRemoveTracksFromEndCurrentLastShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 0);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromBeginningShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);

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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(0)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(1)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(2)));
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
    }

    @Test
    public void testRemoveTracksFromMiddleBeforeAndAfterCurrentShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(0)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(1)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(2)));
    }

    @Test
    public void testRemoveTracksWithoutPos() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        PlaybackQueueItemInstance duplicateItem = new PlaybackQueueItemInstance();
        duplicateItem.setId("track1");
        status.getPlaybackQueue().getItems().add(duplicateItem);
        status.getPlaybackQueue().getOriginallyOrderedItems().add(duplicateItem);

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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
    }

    @Test
    public void testRemoveTracksWithoutPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
        PlaybackQueueItemInstance duplicateItem = new PlaybackQueueItemInstance();
        duplicateItem.setId("track1");
        status.getPlaybackQueue().getItems().add(duplicateItem);
        status.getPlaybackQueue().getOriginallyOrderedItems().add(duplicateItem);

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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 3);
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(0)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(1)));
        Assert.assertTrue(status.getPlaybackQueue().getOriginallyOrderedItems().contains(status.getPlaybackQueue().getItems().get(2)));
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 2);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added2);
        Assert.assertEquals(status.getPlaybackQueue().getId(), request.getPlaylistId());
        Assert.assertEquals(status.getPlaybackQueue().getName(), request.getPlaylistName());
    }

    @Test
    public void testSetTracksWithPlaylistShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 2);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added2);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 2);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0), added1);
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1), added2);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().size(), 0);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksAllBeforeCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveTracksAllAfterCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveTracksAllAfterCurrentPosToEndShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksBeforeToAfterCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track1");
    }

    @Test
    public void testMoveTracksBeforeToAfterCurrentPosToEndShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveTracksAfterToBeforeCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksCurrentToBeforeCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveTracksCurrentToAfterCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track3");
    }

    @Test
    public void testMoveTracksCurrentToEndSpecificShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track3");
    }

    @Test
    public void testMoveTracksCurrentToEndShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
    }

    @Test
    public void testMoveMultipleTracksBeforeToAfterCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track3");
    }

    @Test
    public void testMoveMultipleTracksAfterToBeforeCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveMultipleTracksBothBeforeAndAfterToBeforeCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track5");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track4");
    }

    @Test
    public void testMoveMultipleTracksBothBeforeAndAfterToAfterCurrentPosShuffleActive() {
        PlayerStatus status = getDefaultPlayerStatus(5);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
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
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
        Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
    }

    @Test
    public void testSetPlaybackQueueModeQueueToQueueShuffle() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);

            PlayerCommandService service = new PlayerCommandService(status);

            service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_SHUFFLE));

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_SHUFFLE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertTrue(shuffled);
    }

    @Test
    public void testSetPlaybackQueueModeQueueToQueueRepeatShuffle() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);

            PlayerCommandService service = new PlayerCommandService(status);

            service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE));

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertTrue(shuffled);
    }

    @Test
    public void testSetPlaybackQueueModeQueueShuffleToQueueRepeatShuffle() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);

            PlayerCommandService service = new PlayerCommandService(status);

            service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE));

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertFalse(shuffled);
    }

    @Test
    public void testSetPlaybackQueueModeQueueRepeatShuffleToQueueShuffle() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);

            PlayerCommandService service = new PlayerCommandService(status);

            service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_SHUFFLE));

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_SHUFFLE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertFalse(shuffled);
    }

    @Test
    public void testSetPlaybackQueueModeQueueShuffleToQueue() {
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);
            status.setPlaybackQueuePos(1);
            PlayerCommandService service = new PlayerCommandService(status);

            service.moveTracks(new PlaybackQueueMoveTracksRequest(2, Arrays.asList(new PlaybackQueueItemReference("track1", 0))));
            Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(0));
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");

            service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE));

            Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(1));
            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE);
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
        }
    }

    @Test
    public void testSetPlaybackQueueModeWithEmptyPlaybackQueue_mustSendPlayerStatusChangedNotification_shuffle() {
        PlayerManager player = Mockito.mock(PlayerManager.class);

        PlayerStatus playerStatus = new PlayerStatus(new PlaybackQueue());
        playerStatus.setPlaying(false);
        playerStatus.setSeekPos(0.0);
        playerStatus.setPlaybackQueuePos(0);
        playerStatus.setPlaybackQueueMode(PlaybackQueueMode.QUEUE);

        PlayerCommandService service = new PlayerCommandService(null, player, playerStatus, new Object());

        service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_SHUFFLE));
        Mockito.verify(player).sendPlayerStatusChangedNotification();
        Assert.assertEquals(playerStatus.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_SHUFFLE);
    }

    @Test
    public void testSetPlaybackQueueModeWithEmptyPlaybackQueue_mustSendPlayerStatusChangedNotification_repeat() {
        PlayerManager player = Mockito.mock(PlayerManager.class);

        PlayerStatus playerStatus = new PlayerStatus(new PlaybackQueue());
        playerStatus.setPlaying(false);
        playerStatus.setSeekPos(0.0);
        playerStatus.setPlaybackQueuePos(0);
        playerStatus.setPlaybackQueueMode(PlaybackQueueMode.QUEUE);

        PlayerCommandService service = new PlayerCommandService(null, player, playerStatus, new Object());

        service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_REPEAT));
        Mockito.verify(player).sendPlayerStatusChangedNotification();
        Assert.assertEquals(playerStatus.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_REPEAT);
    }

    @Test
    public void testSetPlaybackQueueModeWithEmptyPlaybackQueue_mustSendPlayerStatusChangedNotification_repeatShuffle() {
        PlayerManager player = Mockito.mock(PlayerManager.class);

        PlayerStatus playerStatus = new PlayerStatus(new PlaybackQueue());
        playerStatus.setPlaying(false);
        playerStatus.setSeekPos(0.0);
        playerStatus.setPlaybackQueuePos(0);
        playerStatus.setPlaybackQueueMode(PlaybackQueueMode.QUEUE);

        PlayerCommandService service = new PlayerCommandService(null, player, playerStatus, new Object());

        service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE));
        Mockito.verify(player).sendPlayerStatusChangedNotification();
        Assert.assertEquals(playerStatus.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);
    }

    @Test
    public void testSetPlaybackQueueModeQueueRepeatShuffleToQueue() {
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);
            status.setPlaybackQueuePos(1);
            PlayerCommandService service = new PlayerCommandService(status);

            service.moveTracks(new PlaybackQueueMoveTracksRequest(2, Arrays.asList(new PlaybackQueueItemReference("track1", 0))));
            Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(0));
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");

            service.setPlaybackQueueMode(new PlaybackQueueModeRequest(PlaybackQueueMode.QUEUE));

            Assert.assertEquals(status.getPlaybackQueuePos(), Integer.valueOf(1));
            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE);
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getItems().get(4).getId(), "track5");
        }
    }

    @Test
    public void testShuffleTracksQueue() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE);

            PlayerCommandService service = new PlayerCommandService(status);

            PlaybackQueueModificationResponse response = service.shuffleTracks();
            Assert.assertNotNull(response);
            Assert.assertTrue(response.getResult());

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), status.getPlaybackQueue().getItems().get(0).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), status.getPlaybackQueue().getItems().get(1).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), status.getPlaybackQueue().getItems().get(2).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), status.getPlaybackQueue().getItems().get(3).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), status.getPlaybackQueue().getItems().get(4).getId());
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertTrue(shuffled);
    }

    @Test
    public void testShuffleTracksQueueRepeat() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_REPEAT);

            PlayerCommandService service = new PlayerCommandService(status);

            PlaybackQueueModificationResponse response = service.shuffleTracks();
            Assert.assertNotNull(response);
            Assert.assertTrue(response.getResult());

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_REPEAT);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), status.getPlaybackQueue().getItems().get(0).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), status.getPlaybackQueue().getItems().get(1).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), status.getPlaybackQueue().getItems().get(2).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), status.getPlaybackQueue().getItems().get(3).getId());
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), status.getPlaybackQueue().getItems().get(4).getId());
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertTrue(shuffled);
    }

    @Test
    public void testShuffleTracksQueueShuffle() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_SHUFFLE);

            PlayerCommandService service = new PlayerCommandService(status);

            PlaybackQueueModificationResponse response = service.shuffleTracks();
            Assert.assertNotNull(response);
            Assert.assertTrue(response.getResult());

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_SHUFFLE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertTrue(shuffled);
    }

    @Test
    public void testShuffleTracksQueueRepeatShuffle() {
        boolean shuffled = false;
        for (int i = 0; i < 50; i++) {
            PlayerStatus status = getDefaultPlayerStatus(5);
            status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);

            PlayerCommandService service = new PlayerCommandService(status);

            PlaybackQueueModificationResponse response = service.shuffleTracks();
            Assert.assertNotNull(response);
            Assert.assertTrue(response.getResult());

            Assert.assertEquals(status.getPlaybackQueueMode(), PlaybackQueueMode.QUEUE_REPEAT_SHUFFLE);
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(0).getId(), "track1");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(1).getId(), "track2");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(2).getId(), "track3");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(3).getId(), "track4");
            Assert.assertEquals(status.getPlaybackQueue().getOriginallyOrderedItems().get(4).getId(), "track5");
            if (!status.getPlaybackQueue().getItems().get(0).getId().equals("track1") ||
                    !status.getPlaybackQueue().getItems().get(1).getId().equals("track2") ||
                    !status.getPlaybackQueue().getItems().get(2).getId().equals("track3") ||
                    !status.getPlaybackQueue().getItems().get(3).getId().equals("track4") ||
                    !status.getPlaybackQueue().getItems().get(4).getId().equals("track5")) {
                shuffled = true;
            }
        }
        Assert.assertTrue(shuffled);
    }

    private PlaybackQueue getDefaultPlaylist(int numOfTracks) {
        PlaybackQueue playbackQueue = new PlaybackQueue();
        playbackQueue.setId("playlist1");
        playbackQueue.setName("Playlist1Name");
        for (int i = 0; i < numOfTracks; i++) {
            PlaybackQueueItemInstance item = new PlaybackQueueItemInstance();
            item.setId("track" + (i + 1));
            playbackQueue.getItems().add(item);
        }
        playbackQueue.setOriginallyOrderedItems(new ArrayList<PlaybackQueueItemInstance>(playbackQueue.getItems()));
        return playbackQueue;
    }

    private PlayerStatus getDefaultPlayerStatus(int numOfTracks) {
        PlayerStatus status = new PlayerStatus(new PlaybackQueue());
        status.setPlaying(true);
        status.setSeekPos(42.3);
        status.setPlaybackQueue(getDefaultPlaylist(numOfTracks));
        status.setPlaybackQueuePos(1);
        status.setPlaybackQueueMode(PlaybackQueueMode.QUEUE);
        return status;
    }
}
