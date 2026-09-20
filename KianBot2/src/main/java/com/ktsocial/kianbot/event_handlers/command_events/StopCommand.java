package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopCommand {

    private final MusicService musicService;

    public StopCommand(MusicService musicService) {
        this.musicService = musicService;
    }

    public void handle(CommandContext context) {
        SlashCommandInteractionEvent event = context.event();
        Guild guild = context.guild();
        if (guild == null) {
            return;
        }
        musicService.stop(guild);

        event.reply("Dừng nhạc :stop_button:").queue();
    }
}
