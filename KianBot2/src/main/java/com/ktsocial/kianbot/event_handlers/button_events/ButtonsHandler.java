package com.ktsocial.kianbot.event_handlers.button_events;

import com.ktsocial.kianbot.event_handlers.common_event_handlers.*;
import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.actionrow.ActionRow;

import java.util.List;

public class ButtonsHandler extends ListenerAdapter {

    private final MusicService musicService;

    public ButtonsHandler(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        Guild guild = event.getGuild();
        if (buttonId == null || guild == null) {
            return;
        }

        TextChannel channel = (TextChannel) event.getChannel();

        List<Button> buttons = CommandButtons.buttons;

        switch (buttonId) {
            case "playPauseBtn" -> {
                boolean paused = musicService.togglePause(guild);
                MessageEmbed playPauseEmbed = PlayPauseEventHandler.BuildEmbed(paused, channel);
                event.replyEmbeds(playPauseEmbed).addComponents(ActionRow.of(buttons)).queue();
            }
            case "nextTrackBtn" -> {
                if (musicService.getCurrentTrack(guild) == null) {
                    event.reply("Không có bài hát đang phát :interrobang:").queue();
                    return;
                }
                musicService.skip(guild);
                MessageEmbed skipEmbed = SkipEventHandler.BuildEmbed(musicService.getCurrentTrack(guild), channel);
                event.replyEmbeds(skipEmbed).addComponents(ActionRow.of(buttons)).queue();
            }
            case "stopBtn" -> {
                musicService.stop(guild);
                event.reply("Dừng nhạc :stop_button:").queue();
            }
            case "repeatBtn" -> {
                boolean repeating = musicService.toggleRepeat(guild);
                MessageEmbed loopEmbed = LoopEventHandler.BuildEmbed(musicService.getCurrentTrack(guild), repeating, channel);
                event.replyEmbeds(loopEmbed).addComponents(ActionRow.of(buttons)).queue();
            }
        }
    }
}