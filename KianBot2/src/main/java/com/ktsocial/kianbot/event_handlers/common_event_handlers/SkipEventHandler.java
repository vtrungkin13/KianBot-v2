package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SkipEventHandler {
    public static MessageEmbed BuildEmbed(GuildMusicManager musicManager, AudioPlayer audioPlayer, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        musicManager.scheduler.setRepeating(false);
        musicManager.scheduler.nextTrack();

        if (audioPlayer.getPlayingTrack() != null) {
            embed.setTitle("Đã chuyển bài");
            embed.addField("Đang phát:", audioPlayer.getPlayingTrack().getInfo().title, false);
        } else {
            embed.setTitle("Không còn bài hát trong hàng đợi");
        }
        return embed.build();
    }
}
