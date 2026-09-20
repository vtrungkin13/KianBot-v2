package com.ktsocial.kianbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackSchedulerTest {

    @Test
    void queueStartsTrackImmediatelyWhenPlayerIsIdle() {
        AudioPlayer player = mock(AudioPlayer.class);
        AudioTrack track = mock(AudioTrack.class);
        when(player.startTrack(track, true)).thenReturn(true);
        TrackScheduler scheduler = new TrackScheduler(player);

        scheduler.queue(track);

        verify(player).startTrack(track, true);
        assertTrue(scheduler.getQueue().isEmpty());
    }

    @Test
    void queueStoresTrackWhenPlayerIsAlreadyPlaying() {
        AudioPlayer player = mock(AudioPlayer.class);
        AudioTrack track = mock(AudioTrack.class);
        when(player.startTrack(track, true)).thenReturn(false);
        TrackScheduler scheduler = new TrackScheduler(player);

        scheduler.queue(track);

        assertEquals(1, scheduler.getQueue().size());
        assertSame(track, scheduler.getQueue().peek());
    }

    @Test
    void nextTrackStartsQueuedTrackWithoutInterruptingIt() {
        AudioPlayer player = mock(AudioPlayer.class);
        AudioTrack track = mock(AudioTrack.class);
        when(player.startTrack(track, true)).thenReturn(false);
        TrackScheduler scheduler = new TrackScheduler(player);
        scheduler.queue(track);

        scheduler.nextTrack();

        verify(player).startTrack(track, false);
        assertTrue(scheduler.getQueue().isEmpty());
    }

    @Test
    void nextTrackStopsPlayerWhenQueueIsEmpty() {
        AudioPlayer player = mock(AudioPlayer.class);
        TrackScheduler scheduler = new TrackScheduler(player);

        scheduler.nextTrack();

        verify(player).stopTrack();
    }

    @Test
    void stopStopsPlayerAndClearsQueue() {
        AudioPlayer player = mock(AudioPlayer.class);
        AudioTrack track = mock(AudioTrack.class);
        when(player.startTrack(track, true)).thenReturn(false);
        TrackScheduler scheduler = new TrackScheduler(player);
        scheduler.queue(track);

        scheduler.stop();

        verify(player).stopTrack();
        assertTrue(scheduler.getQueue().isEmpty());
    }

    @Test
    void pauseAndResumeDelegateToPlayer() {
        AudioPlayer player = mock(AudioPlayer.class);
        when(player.isPaused()).thenReturn(true, false);
        TrackScheduler scheduler = new TrackScheduler(player);

        scheduler.pause();
        scheduler.resume();

        verify(player).setPaused(true);
        verify(player).setPaused(false);
        assertTrue(scheduler.isPaused());
        assertFalse(scheduler.isPaused());
    }

    @Test
    void repeatingRestartsCloneInsteadOfAdvancingQueue() {
        AudioPlayer player = mock(AudioPlayer.class);
        AudioTrack track = mock(AudioTrack.class);
        AudioTrack clone = mock(AudioTrack.class);
        when(track.makeClone()).thenReturn(clone);
        TrackScheduler scheduler = new TrackScheduler(player);
        scheduler.setRepeating(true);

        scheduler.onTrackEnd(player, track, AudioTrackEndReason.FINISHED);

        verify(track).makeClone();
        verify(player).startTrack(clone, false);
        verify(player, never()).stopTrack();
    }

    @Test
    void nonRepeatingTrackEndAdvancesToNextTrack() {
        AudioPlayer player = mock(AudioPlayer.class);
        AudioTrack track = mock(AudioTrack.class);
        AudioTrack next = mock(AudioTrack.class);
        when(player.startTrack(next, true)).thenReturn(false);
        TrackScheduler scheduler = new TrackScheduler(player);
        scheduler.queue(next);

        scheduler.onTrackEnd(player, track, AudioTrackEndReason.FINISHED);

        verify(player).startTrack(next, false);
        assertTrue(scheduler.getQueue().isEmpty());
    }
}
