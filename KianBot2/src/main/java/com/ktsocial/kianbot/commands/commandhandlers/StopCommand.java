package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopCommand extends ListenerAdapter {

    public static void stopCommandHandler(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        musicManager.scheduler.setRepeating(false);
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        event.reply("Dừng nhạc :stop_button:").queue();
    }
}
