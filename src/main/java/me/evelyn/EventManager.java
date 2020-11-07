package me.evelyn;

import me.evelyn.command.commands.servermanagement.Filter;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventManager extends ListenerAdapter {
    public EventManager(ReadyEvent event){
        event.getJDA().addEventListener(new Filter());
    }
}
