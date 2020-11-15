package me.evelyn;

import me.duncte123.botcommons.BotCommons;
import me.evelyn.command.commands.events.DisconnectFromVC;
import me.evelyn.command.commands.events.GuildMemberJoin;
import me.evelyn.command.commands.events.GuildMemberLeave;
import me.evelyn.database.SQLiteDataSource;
import me.evelyn.command.spotifyplayer.ClientCredential;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();
    private final DisconnectFromVC dc = new DisconnectFromVC();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        new Thread(new Runnable() {
            public void run() {
                ClientCredential.clientCredentials_Sync();
                while (true) {
                    try {
                        Thread.sleep(3600000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    ClientCredential.clientCredentials_Sync();
                }
            }
        }).start();
        event.getJDA().addEventListener(new EventManager(event));
        dc.Disconnect(event);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefix = Settings.PREFIXES.computeIfAbsent(guildId, this::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if(raw.equalsIgnoreCase(prefix + "shutdown")){
            if(!user.getId().equals(Config.get("owner_id"))){
                event.getChannel().sendMessage("Buddy. What are you doing?").queue();
                return;
            }

            event.getChannel().sendMessage("Shutting Down").queue();
            LOGGER.info("Shutting Down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if(raw.startsWith(prefix)){
            manager.handle(event, prefix);
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        GuildMemberJoin.GuildMemberJoinEvent(event);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        GuildMemberLeave.onGuildMemberRemove(event);
    }

    private String getPrefix(long guildId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = SQLiteDataSource
                    .getConnection()
                    // language=SQLite
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefix");
    }
}
