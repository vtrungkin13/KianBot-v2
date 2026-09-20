package com.ktsocial.kianbot.event_handlers.leaving_handlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

public final class VoiceUpdateContext {
    private final Guild guild;
    private final Member member;
    private final AudioChannel channelLeft;

    private VoiceUpdateContext(Guild guild, Member member, AudioChannel channelLeft) {
        this.guild = guild;
        this.member = member;
        this.channelLeft = channelLeft;
    }

    public static VoiceUpdateContext from(GuildVoiceUpdateEvent event) {
        return new VoiceUpdateContext(event.getGuild(), event.getMember(), event.getChannelLeft());
    }

    public Guild guild() { return guild; }
    public Member member() { return member; }
    public AudioChannel channelLeft() { return channelLeft; }
}
