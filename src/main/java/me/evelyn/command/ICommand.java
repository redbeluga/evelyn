package me.evelyn.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    String getPackage();

    default List<String> getAliases() {
        return List.of();
    }
}
