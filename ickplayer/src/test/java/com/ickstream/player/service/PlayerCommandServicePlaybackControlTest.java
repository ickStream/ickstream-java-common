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

public class PlayerCommandServicePlaybackControlTest {
    @Test
    public void testGetPlayerStatus() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        PlayerStatusResponse returnedStatus = service.getPlayerStatus();

        Assert.assertEquals(returnedStatus.getPlaying(), status.getPlaying());
        Assert.assertEquals(returnedStatus.getPlaybackQueuePos(), status.getPlaybackQueuePos());
        Assert.assertEquals(returnedStatus.getSeekPos(), status.getSeekPos());
        Assert.assertEquals(returnedStatus.getTrack(), status.getPlaybackQueue().getItems().get(status.getPlaybackQueuePos()));
    }

    @Test
    public void testPlay() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        Boolean play = service.play(true);

        Assert.assertTrue(play);
    }

    @Test
    public void testPlayPause() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        Boolean playing = service.play(true);

        Assert.assertTrue(playing);
        Assert.assertTrue(status.getPlaying());

        playing = service.play(false);

        Assert.assertFalse(playing);
        Assert.assertFalse(status.getPlaying());
    }

    @Test
    public void testPlayEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(0);
        PlayerCommandService service = new PlayerCommandService(status);

        Boolean playing = service.play(true);

        Assert.assertFalse(playing);
        Assert.assertFalse(status.getPlaying());
    }

    @Test
    public void testGetSeekPosition() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        SeekPosition response = service.getSeekPosition();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(1));
        Assert.assertEquals(response.getSeekPos(), 42.3);
    }

    @Test
    public void testGetSeekPositionEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(0);
        PlayerCommandService service = new PlayerCommandService(status);

        SeekPosition response = service.getSeekPosition();

        Assert.assertNotNull(response);
        Assert.assertNull(response.getPlaybackQueuePos());
        Assert.assertNull(response.getSeekPos());
    }

    @Test
    public void testSetSeekPositionPrevious() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        SeekPosition position = new SeekPosition(0, 40.1);

        SeekPosition response = service.setSeekPosition(position);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(response.getSeekPos(), 40.1);
    }

    @Test
    public void testSetSeekPositionCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        SeekPosition position = new SeekPosition(1, 12.1);

        SeekPosition response = service.setSeekPosition(position);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(1));
        Assert.assertEquals(response.getSeekPos(), 12.1);
    }

    @Test
    public void testSetSeekPositionInvalidPlaybackQueuePos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        SeekPosition position = new SeekPosition(3, 40.1);

        try {
            service.setSeekPosition(position);
            Assert.assertTrue(false, "Invalid playback queue position not detected");
        } catch (IllegalArgumentException e) {
        }
    }


    @Test
    public void testGetTrack() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        TrackResponse response = service.getTrack(0);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(response.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(response.getPlaybackQueuePos(), new Integer(0));
        Assert.assertEquals(response.getTrack().getId(), status.getPlaybackQueue().getItems().get(0).getId());
    }

    @Test
    public void testGetTrackCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        TrackResponse response = service.getTrack(null);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(response.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertEquals(response.getPlaybackQueuePos(), status.getPlaybackQueuePos());
        Assert.assertEquals(response.getTrack().getId(), status.getPlaybackQueue().getItems().get(status.getPlaybackQueuePos()).getId());
    }

    @Test
    public void testGetTrackCurrentEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(0);
        PlayerCommandService service = new PlayerCommandService(status);

        TrackResponse response = service.getTrack(null);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistId(), status.getPlaybackQueue().getId());
        Assert.assertEquals(response.getPlaylistName(), status.getPlaybackQueue().getName());
        Assert.assertNull(response.getPlaybackQueuePos());
        Assert.assertNull(response.getTrack());
    }

    @Test
    public void testSetTrack() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        Integer playbackQueuePos = service.setTrack(0);

        Assert.assertEquals(playbackQueuePos, new Integer(0));
        Assert.assertEquals(status.getPlaybackQueuePos(), playbackQueuePos);
        Assert.assertEquals(status.getSeekPos(), 0.0);
    }

    @Test
    public void testSetTrackCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        Integer playbackQueuePos = service.setTrack(1);

        Assert.assertEquals(playbackQueuePos, new Integer(1));
        Assert.assertEquals(status.getPlaybackQueuePos(), playbackQueuePos);
        Assert.assertEquals(status.getSeekPos(), 0.0);
    }

    @Test
    public void testSetTrackInvalidPosition() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        try {
            service.setTrack(3);
            Assert.assertTrue(false, "Invalid position not detected");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testSetTrackMetadata() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().get(0).setText("Track1");
        status.getPlaybackQueue().getItems().get(0).setImage("http://example.com/oldtrack1.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setPlaybackQueuePos(0);
        request.setReplace(true);
        PlaybackQueueItem item = new PlaybackQueueItem();
        item.setId("track1");
        item.setText("My First Track");
        item.setImage("http://example.com/track1.png");
        request.setTrack(item);

        PlaybackQueueItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertEquals(result.getImage(), item.getImage());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), item.getId());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getText(), item.getText());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getImage(), item.getImage());
    }

    @Test
    public void testSetTrackMetadataReplaceWithNull() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().get(0).setText("Track1");
        status.getPlaybackQueue().getItems().get(0).setImage("http://example.com/oldtrack1.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setPlaybackQueuePos(0);
        request.setReplace(true);
        PlaybackQueueItem item = new PlaybackQueueItem();
        item.setId("track1");
        item.setText("My First Track");
        request.setTrack(item);

        PlaybackQueueItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertNull(result.getImage());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), item.getId());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getText(), item.getText());
        Assert.assertNull(status.getPlaybackQueue().getItems().get(0).getImage());
    }

    @Test
    public void testSetTrackMetadataMerge() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().get(0).setText("Track1");
        status.getPlaybackQueue().getItems().get(0).setImage("http://example.com/oldtrack1.png");

        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setPlaybackQueuePos(0);
        request.setReplace(false);
        PlaybackQueueItem item = new PlaybackQueueItem();
        item.setId("track1");
        item.setText("My First Track");
        request.setTrack(item);

        PlaybackQueueItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertEquals(result.getImage(), "http://example.com/oldtrack1.png");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getId(), item.getId());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getText(), item.getText());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(0).getImage(), "http://example.com/oldtrack1.png");
    }

    @Test
    public void testSetTrackMetadataWithoutPlaybackQueuePos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().get(1).setText("Track2");
        status.getPlaybackQueue().getItems().get(1).setImage("http://example.com/oldtrack2.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setReplace(true);
        PlaybackQueueItem item = new PlaybackQueueItem();
        item.setId("track2");
        item.setText("My Second Track");
        item.setImage("http://example.com/track2.png");
        request.setTrack(item);

        PlaybackQueueItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertEquals(result.getImage(), item.getImage());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), item.getId());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getText(), item.getText());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getImage(), item.getImage());
    }

    @Test
    public void testSetTrackMetadataReplaceWithNullWithoutPlaybackQueuePos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().get(1).setText("Track2");
        status.getPlaybackQueue().getItems().get(1).setImage("http://example.com/oldtrack2.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setReplace(true);
        PlaybackQueueItem item = new PlaybackQueueItem();
        item.setId("track2");
        item.setText("My Second Track");
        request.setTrack(item);

        PlaybackQueueItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertNull(result.getImage());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), item.getId());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getText(), item.getText());
        Assert.assertNull(status.getPlaybackQueue().getItems().get(1).getImage());
    }

    @Test
    public void testSetTrackMetadataMergeWithoutPlaybackQueuePos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaybackQueue().getItems().get(1).setText("Track2");
        status.getPlaybackQueue().getItems().get(1).setImage("http://example.com/oldtrack2.png");

        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setReplace(false);
        PlaybackQueueItem item = new PlaybackQueueItem();
        item.setId("track2");
        item.setImage("http://example.com/track2.png");
        request.setTrack(item);

        PlaybackQueueItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), "Track2");
        Assert.assertEquals(result.getImage(), item.getImage());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getId(), item.getId());
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getText(), "Track2");
        Assert.assertEquals(status.getPlaybackQueue().getItems().get(1).getImage(), item.getImage());
    }

    @Test
    public void testGetVolume() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        VolumeResponse response = service.getVolume();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.42);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertFalse(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testGetVolumeMuted() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.setMuted(true);
        PlayerCommandService service = new PlayerCommandService(status);

        VolumeResponse response = service.getVolume();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.42);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertTrue(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeAbsolute() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setVolumeLevel(0.32);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.32);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertFalse(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeAbsoluteAndMuted() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setVolumeLevel(0.32);
        request.setMuted(true);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.32);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertTrue(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeAbsoluteWhenMuted() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.setMuted(true);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setVolumeLevel(0.32);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.32);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertTrue(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeRelativeDecrease() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setRelativeVolumeLevel(-0.1);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getVolumeLevel());
        Assert.assertTrue(response.getVolumeLevel() < 0.33);
        Assert.assertTrue(response.getVolumeLevel() > 0.31);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertFalse(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeRelativeIncrease() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setRelativeVolumeLevel(0.2);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.62);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertFalse(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeRelativeMax() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setRelativeVolumeLevel(0.9);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 1.0);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertFalse(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
    }

    @Test
    public void testSetVolumeRelativeMin() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        VolumeRequest request = new VolumeRequest();
        request.setRelativeVolumeLevel(-0.8);

        VolumeResponse response = service.setVolume(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getVolumeLevel(), 0.0);
        Assert.assertEquals(response.getVolumeLevel(), status.getVolumeLevel());
        Assert.assertFalse(response.getMuted());
        Assert.assertEquals(response.getMuted(), status.getMuted());
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
        status.setPlaying(false);
        status.setVolumeLevel(0.42);
        status.setMuted(false);
        status.setPlaybackQueue(getDefaultPlaylist(numOfTracks));
        if (numOfTracks > 1) {
            status.setSeekPos(42.3);
            status.setPlaybackQueuePos(1);
        } else {
            status.setSeekPos(null);
            status.setPlaybackQueuePos(null);
        }
        return status;
    }
}
