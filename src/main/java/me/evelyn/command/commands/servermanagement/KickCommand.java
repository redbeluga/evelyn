package me.evelyn.command.commands.servermanagement;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Member m = ctx.getMember();
        Member selfMember = ctx.getGuild().getSelfMember();

        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0xf7003e);

        if(m.hasPermission(Permission.KICK_MEMBERS)){
            if(selfMember.hasPermission(Permission.KICK_MEMBERS)) {
                List<Member> mentionedMembers = ctx.getMessage().getMentionedMembers();
                if (!mentionedMembers.isEmpty()) {
                    Member target = mentionedMembers.get(0);
                    String targetname = target.getEffectiveName();
                    if(target.getUser().equals(ctx.getJDA().getSelfUser())){
                        message.setDescription("Haha. Trying to be smooth, huh? You can't make me kick myself");
                    }
                    else {
                        if (m.canInteract(target)) {
                            if (ctx.getArgs().size() > 1) {
                                List<String> reasonArray = new ArrayList<>();
                                for(int i = 1; i < ctx.getArgs().size(); i++){
                                    reasonArray.add(ctx.getArgs().get(i));
                                }
                                String reason = String.join(" ", reasonArray);
                                ModerationDM.kickReason(target.getUser(), reason, ctx.getGuild(), ctx.getChannel());
                                message.setDescription(targetname + " was kicked for " + reason);
                            } else {
                                message.setDescription(targetname + " was kicked");
                            }
                            ctx.getGuild().kick(target).queue();
                        } else {
                            if (target.hasPermission(Permission.ADMINISTRATOR)) {
                                if (m.hasPermission(Permission.ADMINISTRATOR)) {
                                    message.setDescription("Stop trying to backstab your comrades.");
                                } else {
                                    message.setDescription("Are you trying to betray your leader?");
                                }
                            } else {
                                if (target.getId().equals(m.getId())) {
                                    message.setDescription("You really trying to kick yourself, huh?");
                                } else {
                                    message.setDescription("Stop trying to backstab your comrades.");
                                }
                            }
                        }
                    }
                }
                else {
                    message.setDescription("Who am I gonna kick dum dum?");
                }
            }
            else{
                message.setDescription("Give me the permissions to do that then.");
            }
        }
        else{
            message.setDescription("You trying to kick someone without permission?");
        }
        ctx.getChannel().sendMessage(message.build()).queue();
    }

    @Override
    public String getName() {
        return "kick";
    }
}
