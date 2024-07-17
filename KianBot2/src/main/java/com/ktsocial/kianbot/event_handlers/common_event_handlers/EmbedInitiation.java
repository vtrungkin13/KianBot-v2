package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

public class EmbedInitiation {

    public static EmbedBuilder ChannelInitiate(TextChannel channel) {
        String authorName = channel.getJDA().getSelfUser().getName();
        String authorAvtUrl = channel.getJDA().getSelfUser().getAvatarUrl();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(authorName, null, authorAvtUrl);
        embed.setColor(Color.decode("#eba22b"));

        return embed;
    }
}
