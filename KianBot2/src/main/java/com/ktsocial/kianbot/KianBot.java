package com.ktsocial.kianbot;

import com.ktsocial.kianbot.commands.CommandManager;
import com.ktsocial.kianbot.commands.commandhandlers.*;
import com.ktsocial.kianbot.commands.leavinghandlers.AutoLeaving;
import com.ktsocial.kianbot.commands.leavinghandlers.Disconnect;
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
        builder.setActivity(Activity.playing("music!  ||  /help"));

        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);

//        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
//        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        //register commands
        shardManager.addEventListener(new CommandManager(),
                new PlayCommand(),
                new AutoLeaving(),
                new Disconnect(),
                new StopCommand(),
                new SkipCommand(),
                new PauseCommand(),
                new ResumeCommand(),
                new QueueCommand(),
                new NowplayingCommand(),
                new LoopCommand());
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