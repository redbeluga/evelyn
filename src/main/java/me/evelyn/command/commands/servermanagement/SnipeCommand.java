package me.evelyn.command.commands.servermanagement;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class SnipeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
    }

    @Override
    public String getName() {
        return "snipe";
    }
}
