package me.evelyn.command.commands.events;

import me.evelyn.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

import java.util.Random;

public class GuildMemberLeave {
    static String[] normalMessages = {
            "[member] left. I never want to see you again.",
            "The traitorous [member] left. What a snake.",
            "Goodbye [member]. Good luck finding a better server!",
            "Is [member] gone? I was getting sick of them.",
            "Where did [member] go? Well I hope they never come back"
    };
    static String[] memberMessages = {
            "[member] left. Farewell Great Leader.",
    };


    public static void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Random rand = new Random();
        User user = event.getUser();

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(0xf7003e);
        if(user.getId().equals(Config.get("owner_id"))){
            join.setDescription(memberMessages[rand.nextInt(memberMessages.length)].replace("[member]", user.getAsMention()));
        }
        else {
            join.setDescription(normalMessages[rand.nextInt(normalMessages.length)].replace("[member]", user.getAsMention()));
        }
        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
    }
}
