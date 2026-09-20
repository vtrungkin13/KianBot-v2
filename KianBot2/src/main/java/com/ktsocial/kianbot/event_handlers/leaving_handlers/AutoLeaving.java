package com.ktsocial.kianbot.event_handlers.leaving_handlers;

import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;


public class AutoLeaving extends ListenerAdapter {

    private final MusicService musicService;

    public AutoLeaving(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft() == null) {
            return;
        }
        if (event.getMember().getUser().isBot()) {
            return;
        }
        final AudioManager audioManager = event.getGuild().getAudioManager();
        if (audioManager.getConnectedChannel() == null) {
            return;
        }
        if (!event.getChannelLeft().equals(audioManager.getConnectedChannel())) {
            return;
        }

        if (isAlone(event.getGuild())) {
            musicService.stop(event.getGuild());
            audioManager.closeAudioConnection();
        }
    }

    private boolean isAlone(Guild guild) {
        if (guild.getAudioManager().getConnectedChannel() == null) {
            return false;
        }
        return guild.getAudioManager().getConnectedChannel().getMembers().stream()
                .noneMatch(x ->
                        !x.getVoiceState().isDeafened()
                                && !x.getUser().isBot());
    }
}

