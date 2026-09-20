package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;
import com.ktsocial.kianbot.event_handlers.command_events.JoinCommand;
import com.ktsocial.kianbot.event_handlers.command_events.LoopCommand;
import com.ktsocial.kianbot.event_handlers.command_events.NowPlayingCommand;
import com.ktsocial.kianbot.event_handlers.command_events.PlayCommand;
import com.ktsocial.kianbot.event_handlers.command_events.PlayPauseCommand;
import com.ktsocial.kianbot.event_handlers.command_events.QueueCommand;
import com.ktsocial.kianbot.event_handlers.command_events.SkipCommand;
import com.ktsocial.kianbot.event_handlers.command_events.StopCommand;
import com.ktsocial.kianbot.music.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class CommandDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandDispatcher.class);

    private final Map<String, CommandHandler> handlers;

    public CommandDispatcher(MusicService musicService) {
        this.handlers = Map.of(
                "play", new PlayCommand(musicService)::handle,
                "join", new JoinCommand()::handle,
                "pause", new PlayPauseCommand(musicService)::handle,
                "skip", new SkipCommand(musicService)::handle,
                "stop", new StopCommand(musicService)::handle,
                "loop", new LoopCommand(musicService)::handle,
                "queue", new QueueCommand(musicService)::handle,
                "nowplaying", new NowPlayingCommand(musicService)::handle
        );
    }

    public boolean supports(String command) {
        return handlers.containsKey(command);
    }

    public void dispatch(String command, CommandContext context) {
        CommandHandler handler = handlers.get(command);
        if (handler == null) {
            LOGGER.debug("Ignoring unsupported slash command '{}'.", command);
            return;
        }
        handler.handle(context);
    }
}