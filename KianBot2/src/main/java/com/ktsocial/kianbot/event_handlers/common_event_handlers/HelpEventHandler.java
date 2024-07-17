package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class HelpEventHandler {
    public static MessageEmbed BuildEmbed(TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);
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

        return embed.build();
    }
}
