package me.evelyn;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.evelyn.database.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.EnumSet;

public class Evelyn {

    private Evelyn() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                .setColor(0xf7003e)
        );

        JDABuilder.createDefault(Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES
        )
                .disableCache(EnumSet.of(
                        CacheFlag.EMOTE,
                        CacheFlag.ACTIVITY,
                        CacheFlag.CLIENT_STATUS
                ))
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("Music!"))
                .build();
    }

    public static void main(String[] args) throws LoginException, SQLException {
        new Evelyn();
    }
}
