package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;

public class StopEventHandler {
    public static void Handle(GuildMusicManager musicManager) {
        musicManager.scheduler.setRepeating(false);
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
    }
}
