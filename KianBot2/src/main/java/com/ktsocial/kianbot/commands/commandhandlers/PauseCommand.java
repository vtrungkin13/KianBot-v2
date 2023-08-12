package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PauseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("pause")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

            if (musicManager.scheduler.player.isPaused()) {
                event.reply("Nhạc đã được tạm dừng").queue();
            } else {
                musicManager.scheduler.player.setPaused(true);
                event.reply("Tạm dừng nhạc :pause_button:").queue();
            }
        }
    }
}
