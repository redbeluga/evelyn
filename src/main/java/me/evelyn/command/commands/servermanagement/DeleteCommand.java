package me.evelyn.command.commands.servermanagement;

import me.evelyn.Config;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class DeleteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Member member = ctx.getMember();
        TextChannel channel = ctx.getChannel();
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0xf7003e);

        if(!member.hasPermission(Permission.MANAGE_SERVER)){
            channel.sendMessage("What are you trying to hide? Pretty suspicious if you ask me.").queue();
            return;
        }

        List<String> input = ctx.getArgs();
        if(input.size() < 1 || !tryInt(input.get(0))){
            message.setDescription("**Give a number to ducking delete\n**" + Config.get("prefix") + "clear [# of messages]");
            channel.sendMessage(message.build()).queue();
            return;
        }
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getHelp() {
        return "Deletes recently sent messages.";
    }

    public Boolean tryInt(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }
}
