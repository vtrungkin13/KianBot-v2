package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;
import com.ktsocial.kianbot.event_handlers.command_events.HelpCommand;
import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandManager extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private final HelpCommand helpCommand;
    private final CommandValidator validator;
    private final CommandDispatcher dispatcher;

    public CommandManager(MusicService musicService) {
        this.helpCommand = new HelpCommand();
        this.validator = new CommandValidator();
        this.dispatcher = new CommandDispatcher(musicService);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if ("help".equals(command)) {
            execute(command, () -> helpCommand.handle(event), event);
            return;
        }

        if (!dispatcher.supports(command)) {
            LOGGER.debug("Ignoring unsupported slash command '{}'.", command);
            return;
        }

        CommandValidator.ValidationResult validation = validator.validate(event, command);
        if (validation.isIgnored()) {
            LOGGER.debug("Ignoring slash command '{}' because its context is unavailable.", command);
            return;
        }
        if (!validation.isValid()) {
            replyError(event, validation.errorMessage());
            return;
        }

        CommandContext context = validation.context();
        execute(command, () -> dispatcher.dispatch(command, context), event);
    }

    private void execute(String command, Runnable action, SlashCommandInteractionEvent event) {
        try {
            LOGGER.debug("Handling slash command '{}' in guild {}.", command,
                    event.getGuild() == null ? "DM" : event.getGuild().getId());
            action.run();
        } catch (RuntimeException exception) {
            LOGGER.error("Unhandled error while processing slash command '{}'.", command, exception);
            replyError(event, "\u0110\u00e3 x\u1ea3y ra l\u1ed7i khi x\u1eed l\u00fd l\u1ec7nh. Vui l\u00f2ng th\u1eed l\u1ea1i sau.");
        }
    }

    private void replyError(SlashCommandInteractionEvent event, String message) {
        if (!event.isAcknowledged()) {
            event.reply(message).setEphemeral(true).queue(
                    success -> { },
                    error -> LOGGER.warn("Failed to send command error response.", error));
        }
    }
}
