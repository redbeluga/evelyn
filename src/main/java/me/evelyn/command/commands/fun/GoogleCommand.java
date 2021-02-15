package me.evelyn.command.commands.fun;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class GoogleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("I need something to search dummy").queue();
            return;
        }

        channel.sendMessage("http://lmgtfy.com/?q=" + String.join("+", ctx.getArgs())).queue();
    }

    @Override
    public String getName() {
        return "google";
    }

    @Override
    public String getHelp() {
        return "Gives the result of a Google search!";
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }
}
