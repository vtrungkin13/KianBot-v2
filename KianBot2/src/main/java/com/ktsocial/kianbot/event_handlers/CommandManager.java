package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        // help command handler
        if (command.equals("help")) {
            HelpCommand.helpCommandHandler(event);
            // other command check
        } else {
            // user who use the command - command user
            final Member member = event.getMember();
            if (member == null) {
                return;
            }
            // the voice state of command user
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (memberVoiceState == null) {
                return;
            }

            // check command user is in voice channel
            if (!memberVoiceState.inAudioChannel()) {
                event.reply("Báº¡n chÆ°a vÃ o kÃªnh thoáº¡i").queue();
                return;
            } else {
                // get the bot and voice state of bot
                final Member bot = guild.getSelfMember();
                final GuildVoiceState botVoiceState = bot.getVoiceState();
                if (botVoiceState == null) {
                    return;
                }

                if (botVoiceState.inAudioChannel()) {
                    AudioChannelUnion memberVoiceChannel = memberVoiceState.getChannel();
                    AudioChannelUnion botVoiceChannel = botVoiceState.getChannel();
                    if (memberVoiceChannel == null || botVoiceChannel == null) {
                        return;
                    }
                    if (!memberVoiceChannel.equals(botVoiceChannel)) {
                        event.reply("Báº¡n Ä‘ang á»Ÿ khÃ¡c kÃªnh thoáº¡i vá»›i bot").queue();
                        return;
                    }
                } else if (!command.equals("play") && !command.equals("join")) {
                    event.reply("Bot chÆ°a vÃ o kÃªnh thoáº¡i").queue();
                    return;
                }
            }
            switch (command) {
                case "play" -> PlayCommand.playCommandHandler(event);
                case "join" -> JoinCommand.joinCommandHandler(event);
                case "pause" -> PlayPauseCommand.playPauseCommandHandler(event);
                case "skip" -> SkipCommand.skipCommandHandler(event);
                case "stop" -> StopCommand.stopCommandHandler(event);
                case "loop" -> LoopCommand.loopCommandHandler(event);
                case "nowplaying" -> NowPlayingCommand.nowPlayingCommandHandler(event);
                case "queue" -> QueueCommand.queueCommandHandler(event);
            }
        }
    }

    // @Override
    // public void onGuildReady(@NotNull GuildReadyEvent event) {
    // // Clear any guild-specific slash commands to avoid duplicates with global
    // commands
    // event.getGuild().updateCommands().queue();
    // }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        // help command
        commandData.add(Commands.slash("help", "Xem hÆ°á»›ng dáº«n sá»­ dá»¥ng KianBot!"));

        // play command
        OptionData musicUrlOption = new OptionData(OptionType.STRING, "name-or-url", "thÃªm link nháº¡c hoáº·c tÃªn bÃ i hÃ¡t",
                true);
        commandData
                .add(Commands.slash("play", "PhÃ¡t nháº¡c báº±ng link nháº¡c hoáº·c tÃªn bÃ i hÃ¡t!").addOptions(musicUrlOption));

        // join command
        commandData.add(Commands.slash("join", "YÃªu cáº§u bot tham gia kÃªnh thoáº¡i cá»§a báº¡n!"));

        // stop command
        commandData.add(Commands.slash("stop", "Dá»«ng phÃ¡t nháº¡c!"));

        // skip command
        commandData.add(Commands.slash("skip", "Bá» qua bÃ i hÃ¡t hiá»‡n táº¡i!"));

        // pause command
        commandData.add(Commands.slash("pause", "Táº¡m dá»«ng hoáº·c tiáº¿p tá»¥c phÃ¡t nháº¡c!"));

        // queue command
        commandData.add(Commands.slash("queue", "Xem hÃ ng Ä‘á»£i!"));

        // nowplaying command
        commandData.add(Commands.slash("nowplaying", "Xem bÃ i hÃ¡t hiá»‡n táº¡i!"));

        // loop command
        commandData.add(Commands.slash("loop", "Láº·p láº¡i bÃ i hÃ¡t hiá»‡n táº¡i!"));

        event.getJDA().updateCommands().addCommands(commandData).queue(
                success -> LOGGER.info("Registered {} slash commands for JDA instance {}.", commandData.size(), event.getJDA().getSelfUser().getId()),
                error -> LOGGER.error("Failed to register slash commands.", error));
    }
}
