package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopCommand extends ListenerAdapter {

    public static void stopCommandHandler(SlashCommandInteractionEvent event, MusicService musicService) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        musicService.stop(guild);

        event.reply("Dừng nhạc :stop_button:").queue();
    }
}
