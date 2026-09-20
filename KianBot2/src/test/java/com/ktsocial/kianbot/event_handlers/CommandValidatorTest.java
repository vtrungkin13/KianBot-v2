package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfMember;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandValidatorTest {

    private final CommandValidator validator = new CommandValidator();

    @Test
    void queueDoesNotRequireUserToBeInVoice() {
        SlashCommandInteractionEvent event = event(false, false, null, null);

        CommandValidator.ValidationResult result = validator.validate(event, "queue");

        assertTrue(result.isValid());
        assertFalse(result.isIgnored());
    }

    @Test
    void playRequiresUserToBeInVoice() {
        SlashCommandInteractionEvent event = event(false, false, null, null);

        CommandValidator.ValidationResult result = validator.validate(event, "play");

        assertFalse(result.isValid());
        assertEquals("Bạn cần vào kênh thoại trước khi sử dụng lệnh này.", result.errorMessage());
    }

    @Test
    void pauseRequiresSameVoiceChannel() {
        AudioChannelUnion memberChannel = mock(AudioChannelUnion.class);
        AudioChannelUnion botChannel = mock(AudioChannelUnion.class);
        SlashCommandInteractionEvent event = event(true, true, memberChannel, botChannel);

        CommandValidator.ValidationResult result = validator.validate(event, "pause");

        assertFalse(result.isValid());
        assertEquals("Bạn phải ở cùng kênh thoại với bot.", result.errorMessage());
    }

    @Test
    void pauseAcceptsSameVoiceChannel() {
        AudioChannelUnion channel = mock(AudioChannelUnion.class);
        SlashCommandInteractionEvent event = event(true, true, channel, channel);

        CommandValidator.ValidationResult result = validator.validate(event, "pause");

        assertTrue(result.isValid());
        assertNotNull(result.context());
    }

    @Test
    void unknownCommandIsIgnored() {
        SlashCommandInteractionEvent event = mock(SlashCommandInteractionEvent.class);

        CommandValidator.ValidationResult result = validator.validate(event, "unknown");

        assertTrue(result.isIgnored());
        assertFalse(result.isValid());
    }

    private SlashCommandInteractionEvent event(
            boolean memberInVoice,
            boolean botInVoice,
            AudioChannelUnion memberChannel,
            AudioChannelUnion botChannel) {
        SlashCommandInteractionEvent event = mock(SlashCommandInteractionEvent.class);
        Guild guild = mock(Guild.class);
        Member member = mock(Member.class);
        SelfMember selfMember = mock(SelfMember.class);
        GuildVoiceState memberVoiceState = mock(GuildVoiceState.class);
        GuildVoiceState botVoiceState = mock(GuildVoiceState.class);

        when(event.getGuild()).thenReturn(guild);
        when(event.getMember()).thenReturn(member);
        when(guild.getSelfMember()).thenReturn(selfMember);
        when(member.getVoiceState()).thenReturn(memberVoiceState);
        when(selfMember.getVoiceState()).thenReturn(botVoiceState);
        when(memberVoiceState.inAudioChannel()).thenReturn(memberInVoice);
        when(botVoiceState.inAudioChannel()).thenReturn(botInVoice);
        when(memberVoiceState.getChannel()).thenReturn(memberChannel);
        when(botVoiceState.getChannel()).thenReturn(botChannel);

        return event;
    }
}