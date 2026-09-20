package com.ktsocial.kianbot.event_handlers;

import net.dv8tion.jda.api.events.session.ReadyEvent;
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

public class ReadyHandler extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyHandler.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = createCommandData();
        event.getJDA().updateCommands().addCommands(commandData).queue(
                success -> LOGGER.info("Registered {} slash commands for JDA instance {}.",
                        commandData.size(), event.getJDA().getSelfUser().getId()),
                error -> LOGGER.error("Failed to register slash commands.", error));
    }

    private List<CommandData> createCommandData() {
        List<CommandData> commands = new ArrayList<>();
        commands.add(Commands.slash("help", "Xem hướng dẫn sử dụng KianBot!"));
        OptionData musicUrlOption = new OptionData(
                OptionType.STRING, "name-or-url", "Thêm link nhạc hoặc tên bài hát", true);
        commands.add(Commands.slash("play", "Phát nhạc bằng link nhạc hoặc tên bài hát!").addOptions(musicUrlOption));
        commands.add(Commands.slash("join", "Yêu cầu bot tham gia kênh thoại của bạn!"));
        commands.add(Commands.slash("stop", "Dừng phát nhạc!"));
        commands.add(Commands.slash("skip", "Bỏ qua bài hát hiện tại!"));
        commands.add(Commands.slash("pause", "Tạm dừng hoặc tiếp tục phát nhạc!"));
        commands.add(Commands.slash("queue", "Xem hàng đợi!"));
        commands.add(Commands.slash("nowplaying", "Xem bài hát hiện tại!"));
        commands.add(Commands.slash("loop", "Lặp lại bài hát hiện tại!"));
        return commands;
    }
}
