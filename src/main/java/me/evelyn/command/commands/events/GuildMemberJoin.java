package me.evelyn.command.commands.events;

import me.evelyn.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.Random;

public class GuildMemberJoin {
    private static String[] normalMessages = {
            "Yo [member]. You finally came to [server]"
    };

    private static String[] memberMessages = {
            "Welcome to [server], Great Leader [member]."
    };

    public static void GuildMemberJoinEvent( GuildMemberJoinEvent event) {
        Random rand = new Random();
        User user = event.getUser();

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(0xf7003e);
        if(user.getId().equals(Config.get("owner_id"))){
            join.setDescription(memberMessages[rand.nextInt(memberMessages.length)].replace("[member]", event.getMember().getAsMention()).replace("[server]", event.getGuild().getName()));
        }
        else {
            join.setDescription(normalMessages[rand.nextInt(normalMessages.length)].replace("[member]", event.getMember().getAsMention()).replace("[server]", event.getGuild().getName()));
        }
        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
    }
}
