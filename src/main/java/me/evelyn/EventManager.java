package me.evelyn;

import me.evelyn.command.commands.servermanagement.Filter;
import me.evelyn.command.commands.servermanagement.FilterTroll;
import me.evelyn.messagecache.MsgCache;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventManager extends ListenerAdapter {
    public EventManager(ReadyEvent event){
        event.getJDA().addEventListener(new Filter());
        event.getJDA().addEventListener(new MsgCache());
    }
}
