package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class LoopEventHandler {
    public static MessageEmbed BuildEmbed(GuildMusicManager musicManager, TextChannel channel) {
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        AudioTrack track = audioPlayer.getPlayingTrack();
        AudioTrackInfo info = track.getInfo();

        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);
        musicManager.getScheduler().setRepeating(!musicManager.getScheduler().isRepeating());

        if (musicManager.getScheduler().isRepeating()) {
            embed.setTitle("Äang láº·p láº¡i " + info.title);
        } else {
            embed.setTitle("Há»§y láº·p láº¡i " + info.title);
        }

        return embed.build();
    }
}
