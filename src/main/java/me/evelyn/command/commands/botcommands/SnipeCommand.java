package me.evelyn.command.commands.botcommands;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.messagecache.Msg;
import me.evelyn.messagecache.MsgCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SnipeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        long tc_id = ctx.getChannel().getIdLong();
        long guild_id = ctx.getGuild().getIdLong();
        TextChannel channel = ctx.getChannel();

        if(!MsgCache.deletedMsgCache.containsKey(guild_id)){
            channel.sendMessage("There is nothing to snipe!").queue();
            return;
        }

        ArrayList<Msg> cache = MsgCache.deletedMsgCache.get(guild_id);
        Msg designatedMsg = null;

        for(Msg message : cache){
            if(message.getTc_id() == tc_id){
                designatedMsg = message;
                break;
            }
        }

        if (designatedMsg == null){
            channel.sendMessage("There is nothing to snipe!").queue();
            return;
        }

        if (!MsgCache.msgCache.get(ctx.getGuild().getIdLong()).contains(designatedMsg)){
            channel.sendMessage("There is nothing to snipe!").queue();
            return;
        }

        Member member = designatedMsg.getEvent().getMember();
        String messageContent = designatedMsg.getContent();
        Instant time = designatedMsg.getTimeStamp();

        EmbedBuilder snipe = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), "https://www.youtube.com/watch?v=g0hUbEdCNeY", member.getUser().getAvatarUrl())
                .setDescription(messageContent)
                .setColor(0xf7003e)
                .setTimestamp(time);

        channel.sendMessage(snipe.build()).queue();
    }

    @Override
    public String getName() {
        return "snipe";
    }
}
