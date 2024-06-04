package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class LoopCommand extends ListenerAdapter {

    public static void loopCommandHandler(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = track.getInfo();

        List<Button> buttons = CommandButtons.buttons;

        TextChannel channel = (TextChannel) event.getChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(channel.getJDA().getSelfUser().getName(), null, channel.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.decode("#eba22b"));

        musicManager.scheduler.setRepeating(!musicManager.scheduler.isRepeating());
        if (musicManager.scheduler.isRepeating()) {
            embed.setTitle("Đang lặp lại " + info.title);
        }
        else {
            embed.setTitle("Hủy lặp lại " + info.title);
        }
        event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
    }
}
