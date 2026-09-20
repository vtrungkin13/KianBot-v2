package com.ktsocial.kianbot.music;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Application-level facade for guild music state and track loading.
 * Discord event handlers should use this service instead of constructing
 * or accessing the underlying audio player manager directly.
 */
public final class MusicService {
    private static final MusicService INSTANCE = new MusicService();

    private final Map<Long, GuildMusicManager> musicManagers = new ConcurrentHashMap<>();
    private final AudioPlayerManager audioPlayerManager;
    private final TrackLoader trackLoader;

    private MusicService() {
        this.audioPlayerManager = createAudioPlayerManager();
        this.trackLoader = new TrackLoader(this, audioPlayerManager);
    }

    public static MusicService getInstance() {
        return INSTANCE;
    }

    private AudioPlayerManager createAudioPlayerManager() {
        AudioPlayerManager manager = new DefaultAudioPlayerManager();
        manager.registerSourceManager(new YoutubeAudioSourceManager(true));
        AudioSourceManagers.registerRemoteSources(manager);
        return manager;
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
            return musicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        trackLoader.loadAndPlay(channel, trackUrl);
    }
}
