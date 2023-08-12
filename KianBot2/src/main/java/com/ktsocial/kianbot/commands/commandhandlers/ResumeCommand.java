package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ResumeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("resume")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

            if (!musicManager.scheduler.player.isPaused()) {
                event.reply("Nhạc đã được phát").queue();
            } else {
                musicManager.scheduler.player.setPaused(false);
                event.reply("Tiếp tục phát nhạc :arrow_forward:").queue();
            }

        }
    }
}
