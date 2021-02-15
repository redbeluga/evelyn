package me.evelyn.command.commands.music;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;

public class SearchCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getHelp() {
        return "not finished yet!";
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }
}
