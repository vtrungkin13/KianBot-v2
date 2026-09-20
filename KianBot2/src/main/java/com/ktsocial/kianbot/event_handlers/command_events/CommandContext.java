package com.ktsocial.kianbot.event_handlers.command_events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Common Discord context prepared once by CommandManager for guild commands.
 */
public final class CommandContext {

    private final SlashCommandInteractionEvent event;
    private final Guild guild;
    private final Member member;
    private final GuildVoiceState memberVoiceState;
    private final GuildVoiceState botVoiceState;

    public CommandContext(
            SlashCommandInteractionEvent event,
            Guild guild,
            Member member,
            GuildVoiceState memberVoiceState,
            GuildVoiceState botVoiceState) {
        this.event = event;
        this.guild = guild;
        this.member = member;
        this.memberVoiceState = memberVoiceState;
        this.botVoiceState = botVoiceState;
    }

    public SlashCommandInteractionEvent event() {
        return event;
    }

    public Guild guild() {
        return guild;
    }

    public Member member() {
        return member;
    }

    public GuildVoiceState memberVoiceState() {
        return memberVoiceState;
    }

    public GuildVoiceState botVoiceState() {
        return botVoiceState;
    }

    public MessageChannel channel() {
        return event.getChannel();
    }

    public AudioChannel memberVoiceChannel() {
        return memberVoiceState.getChannel();
    }
}
