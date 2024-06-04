package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class ButtonsHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = track.getInfo();

        TextChannel channel = (TextChannel) event.getChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(channel.getJDA().getSelfUser().getName(), null, channel.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.decode("#eba22b"));

        List<Button> buttons = CommandButtons.buttons;

        switch (buttonId) {
            case "playPauseBtn" -> {
                if (!musicManager.scheduler.player.isPaused()) {
                    musicManager.scheduler.player.setPaused(true);
                    embed.setTitle("Tạm dừng nhạc :pause_button:");
                } else {
                    musicManager.scheduler.player.setPaused(false);
                    embed.setTitle("Tiếp tục phát nhạc :arrow_forward:");
                }
                event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
            }
            case "nextTrackBtn" -> {
                if (audioPlayer.getPlayingTrack() == null) {
                    event.reply("Không có bài hát đang phát :interrobang:").queue();
                    return;
                }
                embed.setTitle("Đã chuyển bài");
                musicManager.scheduler.setRepeating(false);
                musicManager.scheduler.nextTrack();

                embed.addField("Đang phát:", audioPlayer.getPlayingTrack().getInfo().title, false);

                event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
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
                    embed.setTitle("Đang lặp lại " + info.title);
                }
                else {
                    embed.setTitle("Hủy lặp lại " + info.title);
                }
                event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
            }
        }
    }
}
