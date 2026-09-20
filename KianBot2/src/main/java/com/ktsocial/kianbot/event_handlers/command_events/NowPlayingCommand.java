package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.music.MusicService;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NowPlayingCommand {

    private final MusicService musicService;

    public NowPlayingCommand(MusicService musicService) {
        this.musicService = musicService;
    }

    public void handle(CommandContext context) {
        SlashCommandInteractionEvent event = context.event();
        Guild guild = context.guild();
        if (guild == null) {
            return;
        }
        final AudioTrack track = musicService.getCurrentTrack(guild);

        if (track == null) {
            event.reply("Không có bài hát đang phát :o:").queue();
            return;
        }
        final AudioTrackInfo info = track.getInfo();
        event.reply("Đang phát :loud_sound:: " + info.title + " - " + info.author).queue();
    }
}
