package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.event_handlers.button_events.CommandButtons;
import com.ktsocial.kianbot.event_handlers.common_event_handlers.SkipEventHandler;
import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.actionrow.ActionRow;

import java.util.List;

public class SkipCommand {

    private final MusicService musicService;

    public SkipCommand(MusicService musicService) {
        this.musicService = musicService;
    }

    public void handle(CommandContext context) {
        SlashCommandInteractionEvent event = context.event();
        Guild guild = context.guild();
        if (guild == null) {
            return;
        }

        if (musicService.getCurrentTrack(guild) == null) {
            event.reply("Không có bài hát đang phát :interrobang:").queue();
            return;
        }
        TextChannel channel = (TextChannel) context.channel();

        musicService.skip(guild);
        MessageEmbed skipEmbed = SkipEventHandler.BuildEmbed(musicService.getCurrentTrack(guild), channel);
        List<Button> buttons = CommandButtons.buttons;

        event.replyEmbeds(skipEmbed).addComponents(ActionRow.of(buttons)).queue();
    }
}
