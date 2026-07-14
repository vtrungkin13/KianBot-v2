package com.ktsocial.kianbot.event_handlers.leaving_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;


public class AutoLeaving extends ListenerAdapter {

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

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        if (isAlone(event.getGuild())) {
            musicManager.scheduler.setRepeating(false);
            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();
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

