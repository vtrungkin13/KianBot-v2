package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.event_handlers.button_events.CommandButtons;
import com.ktsocial.kianbot.event_handlers.common_event_handlers.PlayPauseEventHandler;
import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class PlayPauseCommand extends ListenerAdapter {

    public static void playPauseCommandHandler(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        TextChannel channel = (TextChannel) event.getChannel();
        MessageEmbed playPauseEmbed = PlayPauseEventHandler.BuildEmbed(musicManager, channel);
        List<Button> buttons = CommandButtons.buttons;

        event.replyEmbeds(playPauseEmbed).addActionRow(buttons).queue();
    }
}
