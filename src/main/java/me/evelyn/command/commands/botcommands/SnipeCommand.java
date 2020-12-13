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
        String time = designatedMsg.getTimeStamp();

        EmbedBuilder snipe = new EmbedBuilder()
                .setAuthor(member.getUser().getAsTag(), "https://www.youtube.com/watch?v=g0hUbEdCNeY", member.getUser().getAvatarUrl())
                .setDescription(messageContent)
                .setColor(0xf7003e)
                .setFooter(formatTime(time));

        channel.sendMessage(snipe.build()).queue();
    }

    @Override
    public String getName() {
        return "snipe";
    }

    private String formatTime(String time){
        String formattedTime = "";
        String curDtfTime = getTime();
        if(toDate(time).compareTo(toDate(curDtfTime)) == 0){
            try {
                SimpleDateFormat sdFormat= new SimpleDateFormat("hh:mm a");
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                Date _24HourDt = _24HourSDF.parse(time.split(" ")[1].substring(0, 5));
                if(sdFormat.format(_24HourDt).startsWith("0")){
                    formattedTime = "Today at " + sdFormat.format(_24HourDt).substring(1);
                }
                else {
                    formattedTime = "Today at " + sdFormat.format(_24HourDt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                SimpleDateFormat sdFormat= new SimpleDateFormat("hh:mm a");
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                Date _24HourDt = _24HourSDF.parse(time.split(" ")[1].substring(0, 5));
                if(sdFormat.format(_24HourDt).startsWith("0")){
                    formattedTime = time.split(" ")[0] + " " + sdFormat.format(_24HourDt).substring(1);
                }
                else {
                    formattedTime = time.split(" ")[0] + " " + sdFormat.format(_24HourDt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return formattedTime;
    }

    private Date toDate(String dtfTime){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = sdFormat.parse(dtfTime.split(" ")[0]);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
