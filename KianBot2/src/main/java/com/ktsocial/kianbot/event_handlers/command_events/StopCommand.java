package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.event_handlers.common_event_handlers.StopEventHandler;
import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopCommand extends ListenerAdapter {

    public static void stopCommandHandler(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        StopEventHandler.Handle(musicManager);

        event.reply("Dừng nhạc :stop_button:").queue();
    }
}
