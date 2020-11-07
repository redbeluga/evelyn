package me.evelyn.command.commands.events;

import me.evelyn.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.Random;

public class GuildMemberJoin {
    private static String[] normalMessages = {
            "[member] joined. You must construct additional pylons.",
            "Never gonna give [member] up. Never let [member] down!",
            "Hey! Listen! [member] has joined!",
            "Ha! [member] has joined! You activated my trap card!",
            "We've been expecting you, [member].",
            "It's dangerous to go alone, take [member]!",
            "Swoooosh. [member] just landed.",
            "Brace yourselves. [member] just joined the server.",
            "A wild [member] appeared."
    };

    private static String[] memberMessages = {
            "Welcome to this domain, Great Leader [member]."
    };

    public static void GuildMemberJoinEvent( GuildMemberJoinEvent event) {
        Random rand = new Random();
        User user = event.getUser();

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(0xf7003e);
        if(user.getId().equals(Config.get("owner_id"))){
            join.setDescription(memberMessages[rand.nextInt(memberMessages.length)].replace("[member]", event.getMember().getAsMention()));
        }
        else {
            join.setDescription(normalMessages[rand.nextInt(normalMessages.length)].replace("[member]", event.getMember().getAsMention()));
        }
        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
    }
}
