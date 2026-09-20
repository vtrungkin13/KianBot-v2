package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Set;

public final class CommandValidator {

    private static final Set<String> GUILD_COMMANDS = Set.of(
            "play", "join", "pause", "skip", "stop", "loop", "queue", "nowplaying");
    private static final Set<String> VOICE_COMMANDS = Set.of(
            "play", "join", "pause", "skip", "stop", "loop");
    private static final Set<String> SAME_CHANNEL_COMMANDS = Set.of(
            "pause", "skip", "stop", "loop");

    public ValidationResult validate(SlashCommandInteractionEvent event, String command) {
        if (!GUILD_COMMANDS.contains(command)) {
            return ValidationResult.ignored();
        }

        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (guild == null || member == null) {
            return ValidationResult.ignored();
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();
        GuildVoiceState botVoiceState = guild.getSelfMember().getVoiceState();
        if (memberVoiceState == null || botVoiceState == null) {
            return ValidationResult.failure("Không thể xác định trạng thái kênh thoại. Vui lòng thử lại sau.");
        }

        if (VOICE_COMMANDS.contains(command) && !memberVoiceState.inAudioChannel()) {
            return ValidationResult.failure("Bạn cần vào kênh thoại trước khi sử dụng lệnh này.");
        }

        if (!botVoiceState.inAudioChannel()) {
            if (SAME_CHANNEL_COMMANDS.contains(command)) {
            return ValidationResult.failure("Bot hiện không ở trong kênh thoại.");
            }
            return ValidationResult.success(new CommandContext(
                    event, guild, member, memberVoiceState, botVoiceState));
        }

        if (SAME_CHANNEL_COMMANDS.contains(command) || VOICE_COMMANDS.contains(command)) {
            AudioChannel memberChannel = memberVoiceState.getChannel();
            AudioChannel botChannel = botVoiceState.getChannel();
            if (memberChannel == null || botChannel == null) {
            return ValidationResult.failure("Không thể xác định kênh thoại. Vui lòng thử lại sau.");
            }
            if (!memberChannel.equals(botChannel)) {
            return ValidationResult.failure("Bạn phải ở cùng kênh thoại với bot.");
            }
        }

        return ValidationResult.success(new CommandContext(
                event, guild, member, memberVoiceState, botVoiceState));
    }

    public record ValidationResult(CommandContext context, String errorMessage, boolean ignoredContext) {
        public static ValidationResult success(CommandContext context) {
            return new ValidationResult(context, null, false);
        }

        public static ValidationResult failure(String errorMessage) {
            return new ValidationResult(null, errorMessage, false);
        }

        public static ValidationResult ignored() {
            return new ValidationResult(null, null, true);
        }

        public boolean isIgnored() {
            return ignoredContext;
        }

        public boolean isValid() {
            return context != null;
        }
    }
}
