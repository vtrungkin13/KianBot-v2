package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PlayPauseEventHandler {

    public static MessageEmbed BuildEmbed(GuildMusicManager musicManager, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        if (!musicManager.getScheduler().isPaused()) {
            musicManager.getScheduler().pause();
            embed.setTitle("Táº¡m dá»«ng nháº¡c :pause_button:");
        } else {
            musicManager.getScheduler().resume();
            embed.setTitle("Tiáº¿p tá»¥c phÃ¡t nháº¡c :arrow_forward:");
        }

        return embed.build();
    }
}
