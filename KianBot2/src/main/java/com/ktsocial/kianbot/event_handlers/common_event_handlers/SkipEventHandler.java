package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SkipEventHandler {
    public static MessageEmbed BuildEmbed(AudioTrack currentTrack, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        if (currentTrack != null) {
            embed.setTitle("Đã chuyển bài");
            embed.addField("Đang phát:", currentTrack.getInfo().title, false);
        } else {
            embed.setTitle("Không còn bài hát trong hàng đợi");
        }
        return embed.build();
    }
}
