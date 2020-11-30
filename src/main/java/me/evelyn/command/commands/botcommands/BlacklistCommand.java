package me.evelyn.command.commands.botcommands;

import me.evelyn.Config;
import me.evelyn.Settings;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class BlacklistCommand implements ICommand {
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

            if(!Settings.BLACKLIST.contains(target.getIdLong())) {
                Settings.BLACKLIST.add(target.getIdLong());
                ctx.getChannel().sendMessageFormat("`%s` was blacklisted.", target.getUser().getName()).queue();
            }
            else{
                ctx.getChannel().sendMessageFormat("`%s` is already blacklisted.", target.getUser().getName()).queue();
            }
        }


    }

    @Override
    public String getName() {
        return "blacklist";
    }
}
