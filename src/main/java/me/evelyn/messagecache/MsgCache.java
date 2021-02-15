package me.evelyn.messagecache;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class MsgCache extends ListenerAdapter {
    public static HashMap<Long, ArrayList<Msg>> msgCache = new HashMap<>();
    public static HashMap<Long, ArrayList<Msg>> deletedMsgCache = new HashMap<>();
    private static int cacheSize = 15;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getMember().getUser().isBot()){
            return;
        }

        if(!msgCache.containsKey(event.getGuild().getIdLong())){
            msgCache.put(event.getGuild().getIdLong(), new ArrayList<>());
        }

//        String attachmentUrl = null;
//        if(!event.getMessage().getAttachments().isEmpty()){
//            attachmentUrl = event.getMessage().getAttachments().get(0).getUrl();
//        }

//        msgCache.get(event.getGuild().getIdLong())
//                .add(new Msg(event, event.getMessageIdLong(), event.getChannel().getIdLong(), Instant.now(), attachmentUrl));

        msgCache.get(event.getGuild().getIdLong())
                .add(new Msg(event, event.getMessageIdLong(), event.getChannel().getIdLong(), Instant.now()));

        ArrayList<Msg> same_tc_cache = new ArrayList<>();
        for(Msg message : msgCache.get(event.getGuild().getIdLong())){
            if(message.getTc_id() == event.getChannel().getIdLong())
                same_tc_cache.add(message);
        }

        if (!same_tc_cache.isEmpty() && same_tc_cache.size() > cacheSize){
            msgCache.get(event.getGuild().getIdLong()).remove(0);
        }
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        long msg_id = event.getMessageIdLong();
        long tc_id = event.getChannel().getIdLong();
        long guild_id = event.getGuild().getIdLong();

        if(!deletedMsgCache.containsKey(guild_id)){
            deletedMsgCache.put(event.getGuild().getIdLong(), new ArrayList<Msg>());
        }

        Msg msg = null;

        if(!msgCache.containsKey(guild_id))
            return;

        for(Msg message : msgCache.get(guild_id)){
            if(message.getTc_id() == tc_id && message.getMsg_id() == msg_id)
                msg = message;
        }
        if(msg != null) {
            for (int i = 0; i < deletedMsgCache.get(guild_id).size(); i++){
                if(msg.getTc_id() == deletedMsgCache.get(guild_id).get(i).getTc_id()) {
                    deletedMsgCache.get(guild_id).remove(i);
                    break;
                }
            }
            deletedMsgCache.get(guild_id).add(msg);
        }
    }
}

