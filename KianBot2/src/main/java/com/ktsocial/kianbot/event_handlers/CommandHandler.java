package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;

@FunctionalInterface
public interface CommandHandler {
    void handle(CommandContext context);
}