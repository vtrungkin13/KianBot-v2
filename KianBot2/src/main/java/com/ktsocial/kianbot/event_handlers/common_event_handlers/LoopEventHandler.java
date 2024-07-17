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
        AudioPlayer audioPlayer = musicManager.audioPlayer;
        AudioTrack track = audioPlayer.getPlayingTrack();
        AudioTrackInfo info = track.getInfo();

        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);
        musicManager.scheduler.setRepeating(!musicManager.scheduler.isRepeating());

        if (musicManager.scheduler.isRepeating()) {
            embed.setTitle("Đang lặp lại " + info.title);
        } else {
            embed.setTitle("Hủy lặp lại " + info.title);
        }

        return embed.build();
    }
}
