package me.evelyn.command.commands.servermanagement;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class ModerationDM {
    public static void kickReason(User user, String reason, Guild guild, TextChannel channel) {
        EmbedBuilder message = new EmbedBuilder();
        message.setDescription("You were kicked from " + guild.getName() + " for " + reason.toLowerCase());
        message.setColor(0xf7003e);
        try {
            user.openPrivateChannel().complete().sendMessage(message.build()).complete();
        }
        catch (ErrorResponseException e) {
        }
    }
}
