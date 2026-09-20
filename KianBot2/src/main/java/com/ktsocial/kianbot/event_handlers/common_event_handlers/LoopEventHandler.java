package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class LoopEventHandler {
    public static MessageEmbed BuildEmbed(AudioTrack track, boolean repeating, TextChannel channel) {
        AudioTrackInfo info = track.getInfo();

        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);
        if (repeating) {
            embed.setTitle("Đang lặp lại " + info.title);
        } else {
            embed.setTitle("Dừng lặp lại " + info.title);
        }

        return embed.build();
    }
}
