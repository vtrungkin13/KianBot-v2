package com.ktsocial.kianbot.event_handlers;

import com.ktsocial.kianbot.event_handlers.command_events.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Guild guild = event.getGuild();
        if (guild == null) {
            return;
        }
        //help command handler
        if (command.equals("help")) {
            HelpCommand.helpCommandHandler(event);
            //other command check
        } else {
            //user who use the command - command user
            final Member member = event.getMember();
            if (member == null) {
                return;
            }
            //the voice state of command user
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (memberVoiceState == null) {
                return;
            }

            //check command user is in voice channel
            if (!memberVoiceState.inAudioChannel()) {
                event.reply("Bạn chưa vào kênh thoại").queue();
            } else {
                //get the bot and voice state of bot
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
                        event.reply("Bạn đang ở khác kênh thoại với bot").queue();
                    }
                } else if (!command.equals("play")) {
                    event.reply("Bot chưa vào kênh thoại").queue();
                }
            }
            switch (command) {
                case "pause" -> PlayPauseCommand.playPauseCommandHandler(event);
                case "skip" -> SkipCommand.skipCommandHandler(event);
                case "stop" -> StopCommand.stopCommandHandler(event);
                case "loop" -> LoopCommand.loopCommandHandler(event);
                case "nowplaying" -> NowPlayingCommand.nowPlayingCommandHandler(event);
                case "queue" -> QueueCommand.queueCommandHandler(event);
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        registerCommands(event);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        registerCommands(event);
    }

    private void registerCommands(GenericGuildEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        //help command
        commandData.add(Commands.slash("help", "Xem hướng dẫn sử dụng KianBot!"));

        //play command
        OptionData musicUrlOption = new OptionData(OptionType.STRING, "name-or-url", "thêm link nhạc hoặc tên bài hát", true);
        commandData.add(Commands.slash("play", "Phát nhạc bằng link nhạc hoặc tên bài hát!").addOptions(musicUrlOption));

        //stop command
        commandData.add(Commands.slash("stop", "Dừng phát nhạc!"));

        //skip command
        commandData.add(Commands.slash("skip", "Bỏ qua bài hát hiện tại!"));

        //pause command
        commandData.add(Commands.slash("pause", "Tạm dừng hoặc tiếp tục phát nhạc!"));

        //queue command
        commandData.add(Commands.slash("queue", "Xem hàng đợi!"));

        //nowplaying command
        commandData.add(Commands.slash("nowplaying", "Xem bài hát hiện tại!"));

        //loop command
        commandData.add(Commands.slash("loop", "Lặp lại bài hát hiện tại!"));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
