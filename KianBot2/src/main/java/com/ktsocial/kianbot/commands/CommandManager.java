package com.ktsocial.kianbot.commands;

import com.ktsocial.kianbot.commands.commandhandlers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        //help command handler
        if (command.equals("help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
            embed.setColor(Color.decode("#eba22b"));
            embed.setTitle("Bot using slash command!!");

            String field = """
                    play + link/tênbàihát: phát nhạc
                    skip: bỏ qua bài hát hiện tại
                    stop: dừng phát nhạc
                    pause: tạm dừng nhạc
                    resume: tiếp tục phát nhạc
                    queue: xem hàng đợi
                    nowplaying: xem bài hát hiện tại
                    loop: lặp lại bài hát hiện tại
                    """;

            embed.addField("Commands:", field, false);
            event.replyEmbeds(embed.build()).queue();
            //other command check
        } else {
            //user who use the command - command user
            final Member member = event.getMember();
            //the voice state of command user
            final GuildVoiceState memVoice = member.getVoiceState();

            //check command user is in voice channel
            if (!memVoice.inAudioChannel()) {
                event.reply("Bạn chưa vào kênh thoại").queue();
            } else {
                //get the bot and voice state of bot
                final Member self = event.getGuild().getSelfMember();
                final GuildVoiceState selfVoice = self.getVoiceState();

                if (selfVoice.inAudioChannel()) {
                    if (!memVoice.getChannel().equals(selfVoice.getChannel())) {
                        event.reply("Bạn đang ở khác kênh thoại với bot").queue();
                    }
                }
            }
            switch (command) {
                case "pause" -> {
                    PlayPauseCommand.playPauseCommandHandler(event);
                }
                case "skip" -> {
                    SkipCommand.skipCommandHandler(event);
                }
                case "stop" -> {
                    StopCommand.stopCommandHandler(event);
                }
                case "loop" -> {
                    LoopCommand.loopCommandHandler(event);
                }
                case "nowplaying" -> {
                    NowplayingCommand.nowplayingCommandHandler(event);
                }
                case "queue" -> {
                    QueueCommand.queueCommandHandler(event);
                }
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
