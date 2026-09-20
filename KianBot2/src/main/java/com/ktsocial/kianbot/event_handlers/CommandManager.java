package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.CommandContext;
import com.ktsocial.kianbot.event_handlers.command_events.HelpCommand;
import com.ktsocial.kianbot.event_handlers.command_events.JoinCommand;
import com.ktsocial.kianbot.event_handlers.command_events.LoopCommand;
import com.ktsocial.kianbot.event_handlers.command_events.NowPlayingCommand;
import com.ktsocial.kianbot.event_handlers.command_events.PlayCommand;
import com.ktsocial.kianbot.event_handlers.command_events.PlayPauseCommand;
import com.ktsocial.kianbot.event_handlers.command_events.QueueCommand;
import com.ktsocial.kianbot.event_handlers.command_events.SkipCommand;
import com.ktsocial.kianbot.event_handlers.command_events.StopCommand;
import com.ktsocial.kianbot.music.MusicService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandManager extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private final HelpCommand helpCommand;
    private final JoinCommand joinCommand;
    private final PlayCommand playCommand;
    private final PlayPauseCommand playPauseCommand;
    private final SkipCommand skipCommand;
    private final StopCommand stopCommand;
    private final LoopCommand loopCommand;
    private final QueueCommand queueCommand;
    private final NowPlayingCommand nowPlayingCommand;

    public CommandManager(MusicService musicService) {
        this.helpCommand = new HelpCommand();
        this.joinCommand = new JoinCommand();
        this.playCommand = new PlayCommand(musicService);
        this.playPauseCommand = new PlayPauseCommand(musicService);
        this.skipCommand = new SkipCommand(musicService);
        this.stopCommand = new StopCommand(musicService);
        this.loopCommand = new LoopCommand(musicService);
        this.queueCommand = new QueueCommand(musicService);
        this.nowPlayingCommand = new NowPlayingCommand(musicService);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if ("help".equals(command)) {
            execute(command, () -> helpCommand.handle(event), event);
            return;
        }

        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (guild == null || member == null) {
            return;
        }

        GuildVoiceState memberVoiceState = member.getVoiceState();
        GuildVoiceState botVoiceState = guild.getSelfMember().getVoiceState();
        if (memberVoiceState == null || botVoiceState == null) {
            return;
        }

        if (!validateVoiceContext(event, command, memberVoiceState, botVoiceState)) {
            return;
        }

        CommandContext context = new CommandContext(
                event, guild, member, memberVoiceState, botVoiceState);

        execute(command, () -> dispatch(command, context), event);
    }

    private boolean validateVoiceContext(
            SlashCommandInteractionEvent event,
            String command,
            GuildVoiceState memberVoiceState,
            GuildVoiceState botVoiceState) {
        if (!memberVoiceState.inAudioChannel()) {
            replyError(event, "Bạn cần vào kênh thoại trước khi sử dụng lệnh này.");
            return false;
        }

        if (!botVoiceState.inAudioChannel()) {
            return "play".equals(command) || "join".equals(command);
        }

        AudioChannel memberChannel = memberVoiceState.getChannel();
        AudioChannel botChannel = botVoiceState.getChannel();
        if (memberChannel == null || botChannel == null) {
            return false;
        }

        if (!memberChannel.equals(botChannel)) {
            replyError(event, "Bạn phải ở cùng kênh thoại với bot.");
            return false;
        }

        return true;
    }

    private void dispatch(String command, CommandContext context) {
        switch (command) {
            case "play" -> playCommand.handle(context);
            case "join" -> joinCommand.handle(context);
            case "pause" -> playPauseCommand.handle(context);
            case "skip" -> skipCommand.handle(context);
            case "stop" -> stopCommand.handle(context);
            case "loop" -> loopCommand.handle(context);
            case "nowplaying" -> nowPlayingCommand.handle(context);
            case "queue" -> queueCommand.handle(context);
            default -> LOGGER.debug("Ignoring unknown slash command: {}", command);
        }
    }

    private void execute(String command, Runnable action, SlashCommandInteractionEvent event) {
        try {
            LOGGER.debug("Handling slash command '{}' in guild {}.", command,
                    event.getGuild() == null ? "DM" : event.getGuild().getId());
            action.run();
        } catch (RuntimeException exception) {
            LOGGER.error("Unhandled error while processing slash command '{}'.", command, exception);
            replyError(event, "Đã xảy ra lỗi khi xử lý lệnh. Vui lòng thử lại sau.");
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
