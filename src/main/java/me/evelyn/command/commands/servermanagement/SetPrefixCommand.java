package me.evelyn.command.commands.servermanagement;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.evelyn.Settings;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.database.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if(!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("Why are you trying to change this server setting? Pretty sus.").queue();
            return;
        }

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        final String newPrefix = String.join("", args);

        if(args.isEmpty() || newPrefix.equalsIgnoreCase("$")) {
            updatePrefix(ctx.getGuild().getIdLong(), "$");
            embed.setDescription(ctx.getGuild().getName() + "'s prefix was reset to `$`");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        embed.setDescription("Server prefix set to: `" + newPrefix+ "`");
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    private void updatePrefix(long guildId, String newPrefix) {
        Settings.PREFIXES.put(guildId, newPrefix);

        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}