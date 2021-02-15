package me.evelyn.command.commands.servermanagement;

import java.util.Random;
import me.evelyn.Config;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class FilterTroll extends ListenerAdapter {
    private boolean filterTroll = false;
    private boolean antiBubby = false;
    private boolean maganub = true;
    private String insults[] = {"I wonder if you'd be able to speak more clearly if your parents were second cousins instead of first",
            "I hope you realize everyone's just putting up with you",
            "I wish we were better strangers",
            "Your face looks like someone tried to put out a forest fire with a screwdriver",
            "Anyone who has ever loved you was wrong",
            "May a dozen Siamese elephants step on your testicles while there is a pineapple shoved up your rectum",
            "Go ski into a c***",
            "I hope you outlive your children",
            "You're about as useful as Anne Frank's drum kit",
            "You couldn't pour piss out of a boot if the directions were on the bottom",
            "I don't know what your problem is, but I'm sure it's difficult to pronounce",
            "I don't have the time nor crayons to explain this on your level"
    };
    private int lastNum = -1;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equals("$toggle insults")){
            if (event.getMember().getId().equals(Config.get("owner_id"))) {
                filterTroll = !filterTroll;
                if(filterTroll){
                    event.getChannel().sendMessage("Insults are turned on").queue();
                }
                else{
                    event.getChannel().sendMessage("Insults are turned off").queue();
                }
            }
            else {
                event.getChannel().sendMessage("Piss off?").queue();
            }
        }

        if(event.getMessage().getContentRaw().equals("$toggle insults settings")){
            if (event.getMember().getId().equals(Config.get("owner_id"))) {
                antiBubby = ! antiBubby;
                if( antiBubby){
                    event.getChannel().sendMessage("insults settings are turned on").queue();
                }
                else{
                    event.getChannel().sendMessage("insults settings are turned off").queue();
                }
            }
            else {
                event.getChannel().sendMessage("What you tryna do?").queue();
            }
            return;
        }

        if(event.getMessage().getContentRaw().equals("$toggle maganub")){
            if (event.getMember().getId().equals(Config.get("owner_id"))) {
                maganub = ! maganub;
                if(maganub){
                    event.getChannel().sendMessage("anti-maganub is turned on").queue();
                }
                else{
                    event.getChannel().sendMessage("anti-maganub is turned off").queue();
                }
            }
            else {
                event.getChannel().sendMessage("What you tryna do?").queue();
            }
            return;
        }

        if(filterTroll){
            return;
        }


        if(antiBubby) {
            if (event.getMember().getIdLong() == 538804589894565894L) {
                Random random = new Random();
                int num = random.nextInt(insults.length);
                while (num == lastNum) {
                    num = random.nextInt(insults.length);
                }
                lastNum = num;
                event.getChannel().sendMessage(insults[num]).queue();
            }
        }

        if(maganub){
            String message = event.getMessage().getContentRaw().toLowerCase();
            String[] words = message.split(" ");

            String[] nonoWords = {"nubster", "ganub", "dnub"};

            for(String word : words){
                if (word.contains("ub")){
                    if(word.contains("sad") || word.contains("maga")){
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Don't you dare say that word ever again.").queue();
                        return;
                    }
                }


                for(String nonoWord : nonoWords) {
                    if (word.contains(nonoWord)) {
                        event.getMessage().delete().queue();
                        event.getChannel().sendMessage("Don't you dare say that word ever again.").queue();
                        return;
                    }
                }
            }

            message = message.replaceAll(" ", "");

            if (message.contains("ub")){
                if(message.contains("sad") || message.contains("maga")){
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage("Don't you dare say that word ever again.").queue();
                    return;
                }
            }

            for(String nonoWord : nonoWords) {
                if (message.contains(nonoWord)) {
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage("Don't you dare say that word ever again.").queue();
                    return;
                }
            }



        }
    }
}
