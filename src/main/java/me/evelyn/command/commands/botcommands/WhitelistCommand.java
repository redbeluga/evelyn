package me.evelyn.command.commands.botcommands;

import me.evelyn.Config;
import me.evelyn.Settings;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class WhitelistCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        User user = ctx.getAuthor();

        if(!user.getId().equals(Config.get("owner_id"))){
            ctx.getChannel().sendMessage("Buddy. What are you doing?").queue();
            return;
        }
        List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();
        if (!mentionedMembers.isEmpty()) {
            Member target = mentionedMembers.get(0);
            if(Settings.BLACKLIST.contains(target.getIdLong())){
                Settings.BLACKLIST.remove(target.getIdLong());
                ctx.getChannel().sendMessageFormat("`%s` was whitelisted.", target.getUser().getName()).queue();
            }
            else {
                ctx.getChannel().sendMessageFormat("`%s` was never blacklisted", target.getUser().getName()).queue();
            }
        }
    }

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public String getHelp() {
        return "Takes someone off the blacklist";
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }
}
