package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonsHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = track.getInfo();

        switch (buttonId) {
            case "playPauseBtn" -> {
                if (!musicManager.scheduler.player.isPaused()) {
                    musicManager.scheduler.player.setPaused(true);
                    event.reply("Tạm dừng nhạc :pause_button:").queue();
                } else {
                    musicManager.scheduler.player.setPaused(false);
                    event.reply("Tiếp tục phát nhạc :arrow_forward:").queue();
                }
            }
            case "nextTrackBtn" -> {
                if (audioPlayer.getPlayingTrack() == null) {
                    event.reply("Không có bài hát đang phát :interrobang:").queue();
                    return;
                }

                event.reply(":track_next: Skipped " + audioPlayer.getPlayingTrack().getInfo().title).queue();
                musicManager.scheduler.setRepeating(false);
                musicManager.scheduler.nextTrack();
            }
            case "stopBtn" -> {
                musicManager.scheduler.setRepeating(false);
                musicManager.scheduler.player.stopTrack();
                musicManager.scheduler.queue.clear();

                event.reply("Dừng nhạc :stop_button:").queue();
            }
            case "repeatBtn" -> {
                musicManager.scheduler.setRepeating(!musicManager.scheduler.isRepeating());
                if (musicManager.scheduler.isRepeating()) {
                    event.reply("Đang lặp lại " + info.title).queue();
                }
                else {
                    event.reply("Hủy lặp lại " + info.title).queue();
                }
            }
        }
    }
}
