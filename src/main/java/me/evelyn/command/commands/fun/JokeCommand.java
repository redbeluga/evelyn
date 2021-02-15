package me.evelyn.command.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class JokeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async((json) -> {
            if(!json.get("success").asBoolean()){
                channel.sendMessage("Something went wrong, try again later").queue();
                System.out.println(json);
                return;
            }

            final JsonNode data = json.get("data");
            final String title = data.get("title").asText();
            final String url = data.get("url").asText();
            final String body = data.get("body").asText();

            if(body.length() > 2048){
                handle(ctx);
                return;
            }

            final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle(title, url)
                    .setDescription(body);

            channel.sendMessage(embed.build()).queue();
        });
    }


    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public String getHelp() {
        return "Sends a joke from Reddit!";
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }
}
