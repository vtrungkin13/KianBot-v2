package com.ktsocial.kianbot.music;

import com.ktsocial.kianbot.event_handlers.button_events.CommandButtons;
import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.List;

/** Handles asynchronous LavaPlayer track/playlist loading and user feedback. */
public final class TrackLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackLoader.class);
    private static final List<Button> BUTTONS = CommandButtons.buttons;

    private final MusicService musicService;
    private final AudioPlayerManager audioPlayerManager;

    TrackLoader(MusicService musicService, AudioPlayerManager audioPlayerManager) {
        this.musicService = musicService;
        this.audioPlayerManager = audioPlayerManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        GuildMusicManager musicManager = musicService.getMusicManager(channel.getGuild());
        EmbedBuilder embed = createEmbed(channel);

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.getScheduler().queue(track);
                addTrackField(embed, "ThÃƒÂªm vÃƒÂ o hÃƒÂ ng Ã„â€˜Ã¡Â»Â£i:", track);
                sendEmbed(channel, embed);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();
                if (tracks.isEmpty()) {
                    TrackLoader.this.noMatches(channel);
                    return;
                }

                if (trackUrl.startsWith("ytsearch:")) {
                    AudioTrack track = tracks.getFirst();
                    musicManager.getScheduler().queue(track);
                    addTrackField(embed, "ThÃƒÂªm vÃƒÂ o hÃƒÂ ng Ã„â€˜Ã¡Â»Â£i:", track);
                } else {
                    tracks.forEach(musicManager.getScheduler()::queue);
                    embed.addField("ThÃƒÂªm vÃƒÂ o hÃƒÂ ng Ã„â€˜Ã¡Â»Â£i:",
                            ":notes: " + tracks.size() + " bÃƒÂ i hÃƒÂ¡t tÃ¡Â»Â« " + playlist.getName(), false);
                }

                sendEmbed(channel, embed);
            }

            @Override
            public void noMatches() {
                TrackLoader.this.noMatches(channel);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                LOGGER.warn("Failed to load track '{}': {}", trackUrl, exception.getMessage());
                channel.sendMessage("KhÃƒÂ´ng load Ã„â€˜Ã†Â°Ã¡Â»Â£c nhÃ¡ÂºÂ¡c :x:").queue();
            }
        });
    }

    private EmbedBuilder createEmbed(TextChannel channel) {
        return new EmbedBuilder()
                .setAuthor(channel.getJDA().getSelfUser().getName(), null,
                        channel.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.decode("#eba22b"))
                .setTitle("TÃƒÂ¬m thÃ¡ÂºÂ¥y nhÃ¡ÂºÂ¡c!");
    }

    private void addTrackField(EmbedBuilder embed, String title, AudioTrack track) {
        embed.addField(title,
                ":notes: " + track.getInfo().title + "\n"
                        + track.getInfo().author + formatTime(track.getInfo().length / 1000), false);
    }

    private void sendEmbed(TextChannel channel, EmbedBuilder embed) {
        channel.sendMessageEmbeds(embed.build()).addComponents(ActionRow.of(BUTTONS)).queue();
    }

    private void noMatches(TextChannel channel) {
        channel.sendMessage("KhÃƒÂ´ng tÃƒÂ¬m thÃ¡ÂºÂ¥y nhÃ¡ÂºÂ¡c :o:").queue();
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        return " (%02d:%02d:%02d).".formatted(hours, minutes, remainingSeconds);
    }
}
