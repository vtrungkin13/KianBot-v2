package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.event_handlers.button_events.CommandButtons;
import com.ktsocial.kianbot.event_handlers.common_event_handlers.LoopEventHandler;
import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.actionrow.ActionRow;

import java.util.List;

public class LoopCommand {

    private final MusicService musicService;

    public LoopCommand(MusicService musicService) {
        this.musicService = musicService;
    }

    public void handle(CommandContext context) {
        SlashCommandInteractionEvent event = context.event();
        Guild guild = context.guild();
        if (guild == null) {
            return;
        }

        TextChannel channel = (TextChannel) context.channel();

        AudioTrack track = musicService.getCurrentTrack(guild);
        boolean repeating = musicService.toggleRepeat(guild);
        MessageEmbed loopEmbed = LoopEventHandler.BuildEmbed(track, repeating, channel);
        List<Button> buttons = CommandButtons.buttons;

        event.replyEmbeds(loopEmbed).addComponents(ActionRow.of(buttons)).queue();
    }
}
