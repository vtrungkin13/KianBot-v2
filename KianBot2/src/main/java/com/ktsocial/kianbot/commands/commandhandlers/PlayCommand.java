package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.PlayerManager;
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
        if (command.equals("play")) {
            OptionMapping musicUrlOption = event.getOption("name-or-url");

            //get the message channel which command is used
            final MessageChannel channel = event.getChannel();

            //get the bot and voice state of bot
            final Member self = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoice = self.getVoiceState();

            //get the command user and voice state of command user
            final Member member = event.getMember();
            final GuildVoiceState memVoice = member.getVoiceState();

            //check whether the bot is in voice channel
            if (!selfVoice.inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final AudioChannel memChannel = memVoice.getChannel();
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
