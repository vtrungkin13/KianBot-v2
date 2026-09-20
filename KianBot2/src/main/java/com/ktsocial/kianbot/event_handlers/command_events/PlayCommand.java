package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand {

    private final MusicService musicService;

    public PlayCommand(MusicService musicService) {
        this.musicService = musicService;
    }

    public void handle(CommandContext context) {
        SlashCommandInteractionEvent event = context.event();
        Guild guild = context.guild();
        OptionMapping musicUrlOption = event.getOption("name-or-url");
        if (musicUrlOption == null) {
            event.reply("Lệnh không hợp lệ").queue();
            return;
        }

        //get the message channel which command is used
        final MessageChannel channel = context.channel();

        //get the bot and voice state of bot
        final GuildVoiceState botVoiceState = context.botVoiceState();

        //get the command user and voice state of command user
        final GuildVoiceState memberVoiceState = context.memberVoiceState();

        //check whether the bot is in voice channel
        if (!botVoiceState.inAudioChannel()) {
            final AudioManager audioManager = context.guild().getAudioManager();
            final AudioChannel memChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memChannel);
        }

        String link = musicUrlOption.getAsString();
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        TextChannel textChannel = (TextChannel) channel;
        musicService.loadAndPlay(textChannel, link);
        event.reply("Đang load nhạc!").queue();
    }

    private static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch(URISyntaxException e) {
            return false;
        }
    }
}
