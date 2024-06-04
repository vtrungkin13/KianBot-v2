package com.ktsocial.kianbot.lavaplayer;

import com.ktsocial.kianbot.commands.commandhandlers.CommandButtons;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager ytSource = new YoutubeAudioSourceManager(true);
        this.audioPlayerManager.registerSourceManager(ytSource);

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
//        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    private String convertTime(long time) {
        int t = (int) time;
        int h = t / 3600;
        t = t - 3600 * h;
        int m = t / 60;
        int s = t - 60 * m;

        String hour, min, sec;
        if (h < 10) {
            hour = "0" + h;
        } else {
            hour = String.valueOf(h);
        }

        if (m < 10) {
            min = "0" + m;
        } else {
            min = String.valueOf(m);
        }

        if (s < 10) {
            sec = "0" + s;
        } else {
            sec = String.valueOf(s);
        }

        return " (" + hour + ':' + min + ':' + sec + ").";
    }

    public void loadAndPlay(TextChannel channel, final String trackUrl) {
        final GuildMusicManager musicManager = getMusicManager(channel.getGuild());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(channel.getJDA().getSelfUser().getName(), null, channel.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.decode("#eba22b"));
        embed.setTitle("Tìm thấy nhạc!");

        List<Button> buttons = CommandButtons.buttons;

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                String trackLoadedMessage = ":notes: " + track.getInfo().title + "\n" + track.getInfo().author + convertTime(track.getInfo().length / 1000);

                embed.addField("Thêm vào hàng đợi:", trackLoadedMessage, false);

                channel.sendMessageEmbeds(embed.build()).addActionRow(buttons).queue();

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                if (trackUrl.startsWith("ytsearch:")) {
                    musicManager.scheduler.queue(tracks.get(0));

                    String searchedMusicLoadedMessage = ":notes: " + tracks.get(0).getInfo().title + "\n" + tracks.get(0).getInfo().author + convertTime(tracks.get(0).getInfo().length / 1000);

                    embed.addField("Thêm vào hàng đợi:", searchedMusicLoadedMessage, false);

                    channel.sendMessageEmbeds(embed.build()).addActionRow(buttons).queue();
                } else {

                    for (AudioTrack item : tracks) {
                        musicManager.scheduler.queue(item);
                    }

                    String playlistLoadedMessage = ":notes: " + tracks.size() + " bài hát từ " + playlist.getName();

                    embed.addField("Thêm vào hàng đợi:", playlistLoadedMessage, false);

                    channel.sendMessageEmbeds(embed.build()).addActionRow(buttons).queue();
                }

            }

            @Override
            public void noMatches() {
                channel.sendMessage("Không tìm thấy nhạc :o:").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Không load được nhạc :x:").queue();
            }
        });
    }
}
