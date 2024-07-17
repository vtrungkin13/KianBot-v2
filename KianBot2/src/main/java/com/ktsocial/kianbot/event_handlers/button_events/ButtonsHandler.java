package com.ktsocial.kianbot.event_handlers.button_events;

import com.ktsocial.kianbot.event_handlers.common_event_handlers.*;
import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class ButtonsHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        Guild guild = event.getGuild();
        if (buttonId == null || guild == null) {
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        TextChannel channel = (TextChannel) event.getChannel();

        List<Button> buttons = CommandButtons.buttons;

        switch (buttonId) {
            case "playPauseBtn" -> {
                MessageEmbed playPauseEmbed = PlayPauseEventHandler.BuildEmbed(musicManager, channel);
                event.replyEmbeds(playPauseEmbed).addActionRow(buttons).queue();
            }
            case "nextTrackBtn" -> {
                if (audioPlayer.getPlayingTrack() == null) {
                    event.reply("Không có bài hát đang phát :interrobang:").queue();
                    return;
                }
                MessageEmbed skipEmbed = SkipEventHandler.BuildEmbed(musicManager, audioPlayer, channel);
                event.replyEmbeds(skipEmbed).addActionRow(buttons).queue();
            }
            case "stopBtn" -> {
                StopEventHandler.Handle(musicManager);
                event.reply("Dừng nhạc :stop_button:").queue();
            }
            case "repeatBtn" -> {
                MessageEmbed loopEmbed = LoopEventHandler.BuildEmbed(musicManager, channel);
                event.replyEmbeds(loopEmbed).addActionRow(buttons).queue();
            }
        }
    }
}