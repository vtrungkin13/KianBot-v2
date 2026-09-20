package com.ktsocial.kianbot;

import com.ktsocial.kianbot.event_handlers.CommandManager;
import com.ktsocial.kianbot.event_handlers.button_events.ButtonsHandler;
import com.ktsocial.kianbot.event_handlers.leaving_handlers.AutoLeaving;
import com.ktsocial.kianbot.event_handlers.leaving_handlers.DisconnectEvent;
import com.ktsocial.kianbot.music.MusicService;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import moe.kyokobot.libdave.DaveFactory;
import moe.kyokobot.libdave.NativeDaveFactory;
import moe.kyokobot.libdave.jda.LDJDADaveSessionFactory;

import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KianBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(KianBot.class);

    private final Dotenv config;

    private final ShardManager shardManager;

    /**
     *
     * @throws LoginException when bot token is invalid
     */
    public KianBot() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Missing Discord bot token. Set TOKEN in .env or the environment.");
        }

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("music! || /help"));

        AudioPlayerManager audioPlayerManager = MusicService.createDefaultAudioPlayerManager();
        MusicService musicService = new MusicService(audioPlayerManager);

        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
        try {
            DaveFactory daveFactory = new NativeDaveFactory();
            builder.setAudioModuleConfig(new AudioModuleConfig()
                    .withDaveSessionFactory(new LDJDADaveSessionFactory(daveFactory)));
            LOGGER.info("Discord DAVE (End-to-End Encryption) enabled successfully.");
        } catch (LinkageError | RuntimeException e) {
            LOGGER.warn("Could not enable Discord DAVE voice encryption. Falling back to standard voice connection.", e);
        }

//        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
//        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        //register commands
        shardManager.addEventListener(
                new CommandManager(musicService),
                new AutoLeaving(musicService),
                new DisconnectEvent(musicService),
                new ButtonsHandler(musicService));
    }

    public static void main(String[] args) {
        try {
            new KianBot();
        } catch (LoginException e) {
            LOGGER.error("Unable to log in to Discord. Check the TOKEN value in the environment configuration.", e);
        } catch (IllegalStateException e) {
            LOGGER.error("Bot configuration is invalid: {}", e.getMessage());
        }
    }

    /**
     *
     * @return shardManager instance of the bot
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    public Dotenv getConfig() {
        return config;
    }
}
