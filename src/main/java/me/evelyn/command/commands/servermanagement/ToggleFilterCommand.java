package me.evelyn.command.commands.servermanagement;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.evelyn.Settings;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class ToggleFilterCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();

        if(!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("Why are you trying to change this server setting? Pretty sus.").queue();
            return;
        }

        final EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        final long guildId = ctx.getGuild().getIdLong();
        final boolean toggleStatus = Settings.FILTERTOGGLE.computeIfAbsent(guildId, (id) -> false);
        String statusMessage = "enabled";
        Settings.FILTERTOGGLE.put(guildId, !toggleStatus);
        if(toggleStatus){
            statusMessage = "disabled";
        }

        embed.setDescription("Filter has been " + statusMessage);
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "togglefilter";
    }
}
