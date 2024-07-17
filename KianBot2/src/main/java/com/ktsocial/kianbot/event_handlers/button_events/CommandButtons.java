package com.ktsocial.kianbot.event_handlers.button_events;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class CommandButtons {
    private static final Button playPauseBtn = Button.primary("playPauseBtn", "Tạm dừng")
            .withEmoji(Emoji.fromUnicode("U+23EF U+FE0F"));
    private static final Button nextTrackBtn = Button.primary("nextTrackBtn", "Chuyển bài")
            .withEmoji(Emoji.fromUnicode("U+23ED U+FE0F"));
    private static final Button stopBtn = Button.primary("stopBtn", "Tắt nhạc")
            .withEmoji(Emoji.fromUnicode("U+23F9 U+FE0F"));
    private static final Button repeatBtn = Button.primary("repeatBtn", "Lặp lại")
            .withEmoji(Emoji.fromUnicode("U+1F502"));

    public final static List<Button> buttons = new ArrayList<>(List.of(playPauseBtn, nextTrackBtn, stopBtn, repeatBtn));
}
