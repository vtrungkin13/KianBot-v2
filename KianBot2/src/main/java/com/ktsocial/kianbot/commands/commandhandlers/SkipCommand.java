package com.ktsocial.kianbot.commands.commandhandlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.List;

public class SkipCommand extends ListenerAdapter {

    public static void skipCommandHandler(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            event.reply("Không có bài hát đang phát :interrobang:").queue();
            return;
        }

        List<Button> buttons = CommandButtons.buttons;

        TextChannel channel = (TextChannel) event.getChannel();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(channel.getJDA().getSelfUser().getName(), null, channel.getJDA().getSelfUser().getAvatarUrl());
        embed.setColor(Color.decode("#eba22b"));
        embed.setTitle("Đã chuyển bài");

        musicManager.scheduler.setRepeating(false);
        musicManager.scheduler.nextTrack();

        embed.addField("Đang phát:", audioPlayer.getPlayingTrack().getInfo().title, false);

        event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
    }
}
