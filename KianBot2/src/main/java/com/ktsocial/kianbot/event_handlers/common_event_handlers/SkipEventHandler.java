package com.ktsocial.kianbot.event_handlers.common_event_handlers;

import com.ktsocial.kianbot.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class SkipEventHandler {
    public static MessageEmbed BuildEmbed(GuildMusicManager musicManager, AudioPlayer audioPlayer, TextChannel channel) {
        EmbedBuilder embed = EmbedInitiation.ChannelInitiate(channel);

        musicManager.getScheduler().setRepeating(false);
        musicManager.getScheduler().nextTrack();

        if (audioPlayer.getPlayingTrack() != null) {
            embed.setTitle("ÄÃ£ chuyá»ƒn bÃ i");
            embed.addField("Äang phÃ¡t:", audioPlayer.getPlayingTrack().getInfo().title, false);
        } else {
            embed.setTitle("KhÃ´ng cÃ²n bÃ i hÃ¡t trong hÃ ng Ä‘á»£i");
        }
        return embed.build();
    }
}
