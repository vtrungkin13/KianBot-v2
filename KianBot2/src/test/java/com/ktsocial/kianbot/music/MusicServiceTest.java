package com.ktsocial.kianbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MusicServiceTest {

    private AudioPlayerManager audioPlayerManager;
    private AudioPlayer audioPlayer;
    private Guild guild;
    private MusicService musicService;

    @BeforeEach
    void setUp() {
        audioPlayerManager = mock(AudioPlayerManager.class);
        audioPlayer = mock(AudioPlayer.class);
        guild = mock(Guild.class);
        AudioManager audioManager = mock(AudioManager.class);

        when(audioPlayerManager.createPlayer()).thenReturn(audioPlayer);
        when(guild.getIdLong()).thenReturn(123L);
        when(guild.getAudioManager()).thenReturn(audioManager);

        musicService = new MusicService(audioPlayerManager);
    }

    @Test
    void reusesGuildMusicManagerForSameGuild() {
        assertSame(
                musicService.getMusicManager(guild),
                musicService.getMusicManager(guild)
        );

        verify(audioPlayerManager, times(1)).createPlayer();
    }

    @Test
    void toggleRepeatChangesRepeatState() {
        assertTrue(musicService.toggleRepeat(guild));
        assertFalse(musicService.toggleRepeat(guild));
    }

    @Test
    void disableRepeatAlwaysTurnsRepeatOff() {
        musicService.toggleRepeat(guild);

        musicService.disableRepeat(guild);

        assertTrue(musicService.toggleRepeat(guild));
        assertFalse(musicService.toggleRepeat(guild));
    }

    @Test
    void togglePausePausesThenResumes() {
        when(audioPlayer.isPaused()).thenReturn(false, true);

        assertTrue(musicService.togglePause(guild));
        assertFalse(musicService.togglePause(guild));

        verify(audioPlayer).setPaused(true);
        verify(audioPlayer).setPaused(false);
    }

    @Test
    void queueReturnsSnapshotInsteadOfMutableSchedulerQueue() {
        AudioPlayer currentPlayer = audioPlayer;
        com.sedmelluq.discord.lavaplayer.track.AudioTrack track =
                mock(com.sedmelluq.discord.lavaplayer.track.AudioTrack.class);
        when(currentPlayer.startTrack(track, true)).thenReturn(false);

        musicService.getMusicManager(guild).getScheduler().queue(track);

        List<com.sedmelluq.discord.lavaplayer.track.AudioTrack> queue =
                musicService.getQueue(guild);

        assertEquals(List.of(track), queue);
        assertThrows(UnsupportedOperationException.class, queue::clear);
    }
}
