package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class PlayPauseCommand extends ListenerAdapter {

    public static void playPauseCommandHandler(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        TextChannel channel = (TextChannel) event.getChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(channel.getJDA().getSelfUser().getName(), null, channel.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.decode("#eba22b"));

        List<Button> buttons = CommandButtons.buttons;

        if (!musicManager.scheduler.player.isPaused()) {
            musicManager.scheduler.player.setPaused(true);
            embed.setTitle("Tạm dừng nhạc :pause_button:");
        } else {
            musicManager.scheduler.player.setPaused(false);
            embed.setTitle("Tiếp tục phát nhạc :arrow_forward:");
        }
        event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
    }
}
