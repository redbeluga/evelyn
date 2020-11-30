package me.evelyn.command.commands.servermanagement;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FilterTroll extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(Objects.requireNonNull(event.getMember()).getId().equals("408059895099817986")){
            event.getChannel().sendMessage("unblock kevin pls").queue();
        }
    }
}
