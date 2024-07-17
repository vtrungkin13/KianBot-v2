package com.ktsocial.kianbot.event_handlers.command_events;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.ktsocial.kianbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand extends ListenerAdapter {

    public static void queueCommandHandler(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        if (queue.isEmpty()) {
            event.reply("Hàng đợi trống :o:").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);

        StringBuilder queueMessage = new StringBuilder();

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            queueMessage.append('#')
                    .append(i + 1)
                    .append(" ")
                    .append(info.title)
                    .append(" - ").append(info.author).append("\n");
        }

        if (trackList.size() > trackCount) {
            queueMessage.append(trackList.size() - trackCount)
                    .append(" bài khác...");
        }
        event.reply(":infinity: Hàng đợi:\n" + queueMessage).queue();
    }
}
