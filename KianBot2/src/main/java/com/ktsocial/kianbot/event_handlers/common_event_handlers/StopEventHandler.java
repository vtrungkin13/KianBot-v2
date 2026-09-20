package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;

public class StopEventHandler {
    public static void Handle(GuildMusicManager musicManager) {
        musicManager.getScheduler().setRepeating(false);
        musicManager.getScheduler().stop();
        musicManager.getScheduler().clearQueue();
    }
}
