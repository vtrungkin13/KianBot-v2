package com.ktsocial.kianbot.event_handlers.button_events;

import com.ktsocial.kianbot.event_handlers.common_event_handlers.LoopEventHandler;
import com.ktsocial.kianbot.event_handlers.common_event_handlers.PlayPauseEventHandler;
import com.ktsocial.kianbot.event_handlers.common_event_handlers.SkipEventHandler;
import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ButtonsHandler extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonsHandler.class);

    private static final String PLAY_PAUSE_BUTTON = "playPauseBtn";
    private static final String NEXT_TRACK_BUTTON = "nextTrackBtn";
    private static final String STOP_BUTTON = "stopBtn";
    private static final String REPEAT_BUTTON = "repeatBtn";
    private static final List<Button> BUTTONS = CommandButtons.buttons;

    private final MusicService musicService;

    public ButtonsHandler(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        ButtonContext context = ButtonContext.from(event);
        if (context == null) {
            return;
        }

        try {
            switch (event.getComponentId()) {
                case PLAY_PAUSE_BUTTON -> handlePlayPause(context);
                case NEXT_TRACK_BUTTON -> handleNextTrack(context);
                case STOP_BUTTON -> handleStop(context);
                case REPEAT_BUTTON -> handleRepeat(context);
                default -> LOGGER.debug("Ignoring unknown button interaction: {}", event.getComponentId());
            }
        } catch (RuntimeException exception) {
            LOGGER.error("Unhandled error while processing button '{}'.", event.getComponentId(), exception);
            if (!event.isAcknowledged()) {
                event.reply("Đã xảy ra lỗi khi xử lý thao tác. Vui lòng thử lại sau.")
                        .setEphemeral(true)
                        .queue(null, error -> LOGGER.warn("Failed to send button error response.", error));
            }
        }
    }

    private void handlePlayPause(ButtonContext context) {
        boolean paused = musicService.togglePause(context.guild());
        MessageEmbed embed = PlayPauseEventHandler.BuildEmbed(paused, context.channel());
        replyWithControls(context, embed);
    }

    private void handleNextTrack(ButtonContext context) {
        if (musicService.getCurrentTrack(context.guild()) == null) {
            context.event().reply("Không có bài hát đang phát :interrobang:").queue();
            return;
        }

        musicService.skip(context.guild());
        MessageEmbed embed = SkipEventHandler.BuildEmbed(
                musicService.getCurrentTrack(context.guild()), context.channel());
        replyWithControls(context, embed);
    }

    private void handleStop(ButtonContext context) {
        musicService.stop(context.guild());
        context.event().reply("Dừng nhạc :stop_button:").queue();
    }

    private void handleRepeat(ButtonContext context) {
        boolean repeating = musicService.toggleRepeat(context.guild());
        MessageEmbed embed = LoopEventHandler.BuildEmbed(
                musicService.getCurrentTrack(context.guild()), repeating, context.channel());
        replyWithControls(context, embed);
    }

    private void replyWithControls(ButtonContext context, MessageEmbed embed) {
        context.event().replyEmbeds(embed).addComponents(ActionRow.of(BUTTONS)).queue();
    }
}
