package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PlayPauseEventHandler {

    public static MessageEmbed BuildEmbed(boolean paused, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        if (paused) {
            embed.setTitle("Tạm dừng nhạc :pause_button:");
        } else {
            embed.setTitle("Tiếp tục phát nhạc :arrow_forward:");
        }

        return embed.build();
    }
}
