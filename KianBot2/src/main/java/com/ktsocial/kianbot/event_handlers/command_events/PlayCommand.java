package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        if (command.equals("play")) {
            OptionMapping musicUrlOption = event.getOption("name-or-url");
            if (musicUrlOption == null) {
                event.reply("Lệnh không hợp lệ").queue();
                return;
            }

            //get the message channel which command is used
            final MessageChannel channel = event.getChannel();

            //get the bot and voice state of bot
            final Member bot = guild.getSelfMember();
            final GuildVoiceState botVoiceState = bot.getVoiceState();
            if (botVoiceState == null) {
                return;
            }

            //get the command user and voice state of command user
            final Member member = event.getMember();
            if (member == null) {
                return;
            }
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (memberVoiceState == null) {
                return;
            }

            //check whether the bot is in voice channel
            if (!botVoiceState.inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final AudioChannel memChannel = memberVoiceState.getChannel();
                audioManager.openAudioConnection(memChannel);
            }

            String link = musicUrlOption.getAsString();
            if (!isUrl(link)) {
                link = "ytsearch:" + link;
            }

            TextChannel textChannel = (TextChannel) channel;
            PlayerManager.getInstance().loadAndPlay(textChannel, link);
            event.reply("Đang load nhạc!").queue();
        }
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch(URISyntaxException e) {
            return false;
        }
    }
}