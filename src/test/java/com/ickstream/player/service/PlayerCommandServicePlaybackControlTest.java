/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.service;

import com.ickstream.player.model.PlayerStatus;
import com.ickstream.player.model.Playlist;
import com.ickstream.protocol.device.player.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlayerCommandServicePlaybackControlTest {
    @Test
    public void testGetPlayerStatus() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        PlayerStatusResponse returnedStatus = service.getPlayerStatus();

        Assert.assertEquals(returnedStatus.getPlaying(), status.getPlaying());
        Assert.assertEquals(returnedStatus.getPlaylistPos(), status.getPlaylistPos());
        Assert.assertEquals(returnedStatus.getSeekPos(), status.getSeekPos());
        Assert.assertEquals(returnedStatus.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(returnedStatus.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(returnedStatus.getTrack(), status.getPlaylist().getItems().get(status.getPlaylistPos()));
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
        Assert.assertEquals(response.getPlaylistPos(), new Integer(1));
        Assert.assertEquals(response.getSeekPos(), 42.3);
    }

    @Test
    public void testGetSeekPositionEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(0);
        PlayerCommandService service = new PlayerCommandService(status);

        SeekPosition response = service.getSeekPosition();

        Assert.assertNotNull(response);
        Assert.assertNull(response.getPlaylistPos());
        Assert.assertNull(response.getSeekPos());
    }

    @Test
    public void testSetSeekPositionPrevious() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        SeekPosition position = new SeekPosition(0, 40.1);

        SeekPosition response = service.setSeekPosition(position);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(response.getSeekPos(), 40.1);
    }

    @Test
    public void testSetSeekPositionCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        SeekPosition position = new SeekPosition(1, 12.1);

        SeekPosition response = service.setSeekPosition(position);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistPos(), new Integer(1));
        Assert.assertEquals(response.getSeekPos(), 12.1);
    }

    @Test
    public void testSetSeekPositionInvalidPlaylistPos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);
        SeekPosition position = new SeekPosition(3, 40.1);

        try {
            service.setSeekPosition(position);
            Assert.assertTrue(false, "Invalid playlist position not detected");
        } catch (IllegalArgumentException e) {
        }
    }


    @Test
    public void testGetTrack() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        TrackResponse response = service.getTrack(0);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(response.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(response.getPlaylistPos(), new Integer(0));
        Assert.assertEquals(response.getTrack().getId(), status.getPlaylist().getItems().get(0).getId());
    }

    @Test
    public void testGetTrackCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        TrackResponse response = service.getTrack(null);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(response.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertEquals(response.getPlaylistPos(), status.getPlaylistPos());
        Assert.assertEquals(response.getTrack().getId(), status.getPlaylist().getItems().get(status.getPlaylistPos()).getId());
    }

    @Test
    public void testGetTrackCurrentEmptyPlaylist() {
        PlayerStatus status = getDefaultPlayerStatus(0);
        PlayerCommandService service = new PlayerCommandService(status);

        TrackResponse response = service.getTrack(null);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getPlaylistId(), status.getPlaylist().getId());
        Assert.assertEquals(response.getPlaylistName(), status.getPlaylist().getName());
        Assert.assertNull(response.getPlaylistPos());
        Assert.assertNull(response.getTrack());
    }

    @Test
    public void testSetTrack() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        Integer playlistPos = service.setTrack(0);

        Assert.assertEquals(playlistPos, new Integer(0));
        Assert.assertEquals(status.getPlaylistPos(), playlistPos);
        Assert.assertEquals(status.getSeekPos(), 0.0);
    }

    @Test
    public void testSetTrackCurrent() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        PlayerCommandService service = new PlayerCommandService(status);

        Integer playlistPos = service.setTrack(1);

        Assert.assertEquals(playlistPos, new Integer(1));
        Assert.assertEquals(status.getPlaylistPos(), playlistPos);
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
        status.getPlaylist().getItems().get(0).setText("Track1");
        status.getPlaylist().getItems().get(0).setImage("http://example.com/oldtrack1.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setPlaylistPos(0);
        request.setReplace(true);
        PlaylistItem item = new PlaylistItem();
        item.setId("track1");
        item.setText("My First Track");
        item.setImage("http://example.com/track1.png");
        request.setTrack(item);

        PlaylistItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertEquals(result.getImage(), item.getImage());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getId(), item.getId());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getText(), item.getText());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getImage(), item.getImage());
    }

    @Test
    public void testSetTrackMetadataReplaceWithNull() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().get(0).setText("Track1");
        status.getPlaylist().getItems().get(0).setImage("http://example.com/oldtrack1.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setPlaylistPos(0);
        request.setReplace(true);
        PlaylistItem item = new PlaylistItem();
        item.setId("track1");
        item.setText("My First Track");
        request.setTrack(item);

        PlaylistItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertNull(result.getImage());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getId(), item.getId());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getText(), item.getText());
        Assert.assertNull(status.getPlaylist().getItems().get(0).getImage());
    }

    @Test
    public void testSetTrackMetadataMerge() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().get(0).setText("Track1");
        status.getPlaylist().getItems().get(0).setImage("http://example.com/oldtrack1.png");

        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setPlaylistPos(0);
        request.setReplace(false);
        PlaylistItem item = new PlaylistItem();
        item.setId("track1");
        item.setText("My First Track");
        request.setTrack(item);

        PlaylistItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertEquals(result.getImage(), "http://example.com/oldtrack1.png");
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getId(), item.getId());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getText(), item.getText());
        Assert.assertEquals(status.getPlaylist().getItems().get(0).getImage(), "http://example.com/oldtrack1.png");
    }

    @Test
    public void testSetTrackMetadataWithoutPlaylistPos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().get(1).setText("Track2");
        status.getPlaylist().getItems().get(1).setImage("http://example.com/oldtrack2.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setReplace(true);
        PlaylistItem item = new PlaylistItem();
        item.setId("track2");
        item.setText("My Second Track");
        item.setImage("http://example.com/track2.png");
        request.setTrack(item);

        PlaylistItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertEquals(result.getImage(), item.getImage());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getId(), item.getId());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getText(), item.getText());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getImage(), item.getImage());
    }

    @Test
    public void testSetTrackMetadataReplaceWithNullWithoutPlaylistPos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().get(1).setText("Track2");
        status.getPlaylist().getItems().get(1).setImage("http://example.com/oldtrack2.png");
        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setReplace(true);
        PlaylistItem item = new PlaylistItem();
        item.setId("track2");
        item.setText("My Second Track");
        request.setTrack(item);

        PlaylistItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), item.getText());
        Assert.assertNull(result.getImage());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getId(), item.getId());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getText(), item.getText());
        Assert.assertNull(status.getPlaylist().getItems().get(1).getImage());
    }

    @Test
    public void testSetTrackMetadataMergeWithoutPlaylistPos() {
        PlayerStatus status = getDefaultPlayerStatus(3);
        status.getPlaylist().getItems().get(1).setText("Track2");
        status.getPlaylist().getItems().get(1).setImage("http://example.com/oldtrack2.png");

        PlayerCommandService service = new PlayerCommandService(status);
        TrackMetadataRequest request = new TrackMetadataRequest();
        request.setReplace(false);
        PlaylistItem item = new PlaylistItem();
        item.setId("track2");
        item.setImage("http://example.com/track2.png");
        request.setTrack(item);

        PlaylistItem result = service.setTrackMetadata(request);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), item.getId());
        Assert.assertEquals(result.getText(), "Track2");
        Assert.assertEquals(result.getImage(), item.getImage());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getId(), item.getId());
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getText(), "Track2");
        Assert.assertEquals(status.getPlaylist().getItems().get(1).getImage(), item.getImage());
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
        status.setPlaying(false);
        status.setVolumeLevel(0.42);
        status.setMuted(false);
        status.setPlaylist(getDefaultPlaylist(numOfTracks));
        if (numOfTracks > 1) {
            status.setSeekPos(42.3);
            status.setPlaylistPos(1);
        } else {
            status.setSeekPos(null);
            status.setPlaylistPos(null);
        }
        return status;
    }
}
