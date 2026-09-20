package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.event_handlers.common_event_handlers.HelpEventHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HelpCommand {
    public void handle(SlashCommandInteractionEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        MessageEmbed helpEmbed = HelpEventHandler.BuildEmbed(channel);

        event.replyEmbeds(helpEmbed).queue();
    }
}
