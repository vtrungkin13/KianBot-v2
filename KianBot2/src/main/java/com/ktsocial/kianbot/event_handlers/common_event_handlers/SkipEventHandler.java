package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SkipEventHandler {
    public static MessageEmbed BuildEmbed(AudioTrack currentTrack, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        if (currentTrack != null) {
            embed.setTitle("ÄÃ£ chuyá»ƒn bÃ i");
            embed.addField("Äang phÃ¡t:", currentTrack.getInfo().title, false);
        } else {
            embed.setTitle("KhÃ´ng cÃ²n bÃ i hÃ¡t trong hÃ ng Ä‘á»£i");
        }
        return embed.build();
    }
}
