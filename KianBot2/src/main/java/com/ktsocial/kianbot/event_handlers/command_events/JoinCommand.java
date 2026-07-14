package com.ktsocial.kianbot.event_handlers.command_events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand {

    public static void joinCommandHandler(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }

        final Member member = event.getMember();
        if (member == null) {
            return;
        }
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            event.reply("Bạn chưa vào kênh thoại").queue();
            return;
        }

        final AudioManager audioManager = guild.getAudioManager();
        final AudioChannel memChannel = memberVoiceState.getChannel();
        audioManager.openAudioConnection(memChannel);

        event.reply("Đã kết nối vào kênh thoại!").queue();
    }
}
