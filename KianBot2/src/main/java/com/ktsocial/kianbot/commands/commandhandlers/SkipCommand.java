package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SkipCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("skip")) {

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if (audioPlayer.getPlayingTrack() == null) {
                event.reply("Không có bài hát đang phát :interrobang:").queue();
                return;
            }

            event.reply(":track_next: Skipped " + audioPlayer.getPlayingTrack().getInfo().title).queue();
            musicManager.scheduler.setRepeating(false);
            musicManager.scheduler.nextTrack();
        }
    }
}
