package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.event_handlers.button_events.CommandButtons;
import com.ktsocial.kianbot.event_handlers.common_event_handlers.SkipEventHandler;
import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class SkipCommand extends ListenerAdapter {

    public static void skipCommandHandler(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            event.reply("Không có bài hát đang phát :interrobang:").queue();
            return;
        }
        TextChannel channel = (TextChannel) event.getChannel();

        MessageEmbed skipEmbed = SkipEventHandler.BuildEmbed(musicManager, audioPlayer, channel);
        List<Button> buttons = CommandButtons.buttons;

        event.replyEmbeds(skipEmbed).addActionRow(buttons).queue();
    }
}
