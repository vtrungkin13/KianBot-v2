package com.ktsocial.kianbot.event_handlers.leaving_handlers;

import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class DisconnectEvent extends ListenerAdapter {

    private final MusicService musicService;

    public DisconnectEvent(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        VoiceUpdateContext context = VoiceUpdateContext.from(event);
        final Member self = context.guild().getSelfMember();
        if (context.member().equals(self)) {
            musicService.disableRepeat(context.guild());
        }
    }
}
