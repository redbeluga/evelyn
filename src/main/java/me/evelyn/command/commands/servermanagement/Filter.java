package me.evelyn.command.commands.servermanagement;

import me.evelyn.Config;
import me.evelyn.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class Filter extends ListenerAdapter {
    String[] warnings = {
            "Foul mouthed pig. Wash your mouth",
            "Disgusting potty mouth. Never say that word.",
            "Don't let your mom hear you say these things.",
            "Watch yo vernacular. I don't want to hear that again.",
            "Say that again and watch if I don't sew your lips shut.",
            "Your gonna end up in the trash can one day if you don't stop saying those words."
    };
    String[] badWordsList = {
            "anal",
            "assfuck",
            "asshole",
            "assfucker",
            "asshole",
            "assshole",
            "bastard",
            "bitch",
            "bloodyhell",
            "boong",
            "cockfucker",
            "cocksuck",
            "cocksucker",
            "coon",
            "coonnass",
            "cunt",
            "cyberfuck",
            "dipshit",
            "douche",
            "erotic",
            "escort",
            "fag",
            "faggot",
            "fuck",
            "fuckoff",
            "fuckyou",
            "fuckass",
            "fuckhole",
            "fuk",
            "gook",
            "homoerotic",
            "hentai",
            "motherfucker",
            "motherfuck",
            "motherfucker",
            "negro",
            "nigger",
            "orgasim",
            "orgasm",
            "penis",
            "penisfucker",
            "porn",
            "porno",
            "pornography",
            "pussy",
            "retarded",
            "sadist",
            "sex",
            "slut",
            "sonofabitch",
            "viagra",
            "whore"};

    String[] specialWords = {
            "hoe",
    };

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Random rand = new Random();
        EmbedBuilder warning = new EmbedBuilder();
        warning.setColor(0xf7003e);

        final long guildId = event.getGuild().getIdLong();
        Boolean toggle = Settings.FILTERTOGGLE.computeIfAbsent(guildId, (id) -> false);

        if(toggle) {
            if(!event.getAuthor().isBot()) {
                int warnNum = rand.nextInt(warnings.length);

                String[] messageWords = event.getMessage().getContentRaw().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").split("\\s+");
                String message = event.getMessage().getContentRaw().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").replaceAll("\\s+", "");
                for(String badWord : badWordsList) {
                    if(badWord == "sex") {
                        if(message.contains("sexy")) {
                            break;
                        }
                    }

                    if(message.contains(badWord)) {
                        event.getMessage().delete().queue();
                        warning.setFooter(warnings[warnNum], event.getMember().getUser().getAvatarUrl());
                        event.getChannel().sendMessage(warning.build()).queue();
                        break;
                    }
                }

                for(String word : specialWords) {
                    for(String messageWord : messageWords) {
                        if(messageWord.contains(word)) {
                            if(word.charAt(0) == 'h') {
                                if(messageWord.charAt(0) == 's' || messageWord.charAt(0) == 'c' || messageWord.charAt(0) == 't')
                                    break;
                                else {
                                    event.getMessage().delete().queue();
                                    warning.setFooter(warnings[warnNum], event.getMember().getUser().getAvatarUrl());
                                    event.getChannel().sendMessage(warning.build()).queue();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
