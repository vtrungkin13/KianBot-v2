package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NowplayingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("nowplaying")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;
            final AudioTrack track = audioPlayer.getPlayingTrack();

            if (track == null) {
                event.reply("Không có bài hát đang phát :o:").queue();
                return;
            }
            final AudioTrackInfo info = track.getInfo();
            event.reply("Đang phát :loud_sound:: " + info.title + " - " + info.author).queue();
        }
    }
}
