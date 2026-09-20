package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;
import com.ktsocial.kianbot.music.MusicService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandDispatcherTest {

    @Test
    void supportsAllRegisteredMusicCommands() {
        CommandDispatcher dispatcher = new CommandDispatcher(mock(MusicService.class));

        assertTrue(dispatcher.supports("play"));
        assertTrue(dispatcher.supports("join"));
        assertTrue(dispatcher.supports("pause"));
        assertTrue(dispatcher.supports("skip"));
        assertTrue(dispatcher.supports("stop"));
        assertTrue(dispatcher.supports("loop"));
        assertTrue(dispatcher.supports("queue"));
        assertTrue(dispatcher.supports("nowplaying"));
        assertFalse(dispatcher.supports("help"));
        assertFalse(dispatcher.supports("unknown"));
    }

    @Test
    void dispatchUnknownCommandDoesNotThrow() {
        CommandDispatcher dispatcher = new CommandDispatcher(mock(MusicService.class));

        assertDoesNotThrow(() -> dispatcher.dispatch("unknown", mock(CommandContext.class)));
    }
}