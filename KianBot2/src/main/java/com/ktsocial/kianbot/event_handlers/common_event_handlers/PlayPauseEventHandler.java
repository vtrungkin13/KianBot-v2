package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PlayPauseEventHandler {

    public static MessageEmbed BuildEmbed(GuildMusicManager musicManager, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        if (!musicManager.scheduler.player.isPaused()) {
            musicManager.scheduler.player.setPaused(true);
            embed.setTitle("Tạm dừng nhạc :pause_button:");
        } else {
            musicManager.scheduler.player.setPaused(false);
            embed.setTitle("Tiếp tục phát nhạc :arrow_forward:");
        }

        return embed.build();
    }
}
