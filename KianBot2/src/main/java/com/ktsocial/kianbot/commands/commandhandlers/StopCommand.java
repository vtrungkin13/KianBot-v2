package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("stop")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            musicManager.scheduler.setRepeating(false);
            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();

            event.reply("Dừng nhạc :stop_button:").queue();
        }
    }
}
