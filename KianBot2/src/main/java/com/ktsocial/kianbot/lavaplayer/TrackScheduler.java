package com.ktsocial.kianbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean repeating = false;


    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public void nextTrack() {
        AudioTrack nextTrack = this.queue.poll();
        if (nextTrack != null) {
            this.player.startTrack(nextTrack, false);
        } else {
            this.player.stopTrack();
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioTrack getPlayingTrack() {
        return player.getPlayingTrack();
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void clearQueue() {
        queue.clear();
    }

    public void stop() {
        player.stopTrack();
        queue.clear();
    }

    public void pause() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (repeating) {
                player.startTrack(track.makeClone(), false);
            } else {
                nextTrack();
            }
        }
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        if (!this.player.isPaused()) {
            this.player.setPaused(true);
        }
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        if (this.player.isPaused()) {
            this.player.setPaused(false);
        }
    }
}
