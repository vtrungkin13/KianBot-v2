package com.ktsocial.kianbot;

import com.ktsocial.kianbot.event_handlers.CommandManager;
import com.ktsocial.kianbot.event_handlers.button_events.ButtonsHandler;
import com.ktsocial.kianbot.event_handlers.command_events.PlayCommand;
import com.ktsocial.kianbot.event_handlers.leaving_handlers.AutoLeaving;
import com.ktsocial.kianbot.event_handlers.leaving_handlers.DisconnectEvent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class KianBot {

    private final Dotenv config;

    private final ShardManager shardManager;

    /**
     *
     * @throws LoginException when bot token is invalid
     */
    public KianBot() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("music! || /help"));

        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);

//        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
//        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        //register commands
        shardManager.addEventListener(
                new CommandManager(),
                new PlayCommand(),
                new AutoLeaving(),
                new DisconnectEvent(),
                new ButtonsHandler());
    }

    public static void main(String[] args) {
        try {
            KianBot bot = new KianBot();
        } catch (LoginException e) {
            System.out.println("Error: bot token invalid!!!");
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