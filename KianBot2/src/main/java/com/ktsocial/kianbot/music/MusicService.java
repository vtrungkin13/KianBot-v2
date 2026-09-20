package com.ktsocial.kianbot.music;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Application-level facade for guild music state and track loading.
 * Discord event handlers should use this service instead of constructing
 * or accessing the underlying audio player manager directly.
 */
public final class MusicService {
        private final Map<Long, GuildMusicManager> musicManagers = new ConcurrentHashMap<>();
    private final AudioPlayerManager audioPlayerManager;
    private final TrackLoader trackLoader;

    public MusicService(AudioPlayerManager audioPlayerManager) {
        this.audioPlayerManager = audioPlayerManager;
        this.trackLoader = new TrackLoader(this, audioPlayerManager);
    }

    public static AudioPlayerManager createDefaultAudioPlayerManager() {
        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        manager.registerSourceManager(new YoutubeAudioSourceManager(true));
        AudioSourceManagers.registerRemoteSources(manager);
        return manager;
    }

    GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
            return musicManager;
        });
    }

    public AudioTrack getCurrentTrack(Guild guild) {
        return getMusicManager(guild).getScheduler().getPlayingTrack();
    }

    public List<AudioTrack> getQueue(Guild guild) {
        return List.copyOf(getMusicManager(guild).getScheduler().getQueue());
    }

    public boolean toggleRepeat(Guild guild) {
        var scheduler = getMusicManager(guild).getScheduler();
        scheduler.setRepeating(!scheduler.isRepeating());
        return scheduler.isRepeating();
    }

    public void disableRepeat(Guild guild) {
        getMusicManager(guild).getScheduler().setRepeating(false);
    }

    public boolean togglePause(Guild guild) {
        var scheduler = getMusicManager(guild).getScheduler();
        if (scheduler.isPaused()) {
            scheduler.resume();
            return false;
        }
        scheduler.pause();
        return true;
    }

    public void skip(Guild guild) {
        var scheduler = getMusicManager(guild).getScheduler();
        scheduler.setRepeating(false);
        scheduler.nextTrack();
    }

    public void stop(Guild guild) {
        var scheduler = getMusicManager(guild).getScheduler();
        scheduler.setRepeating(false);
        scheduler.stop();
        scheduler.clearQueue();
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        trackLoader.loadAndPlay(channel, trackUrl);
    }
}
