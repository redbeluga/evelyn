package me.evelyn.messagecache;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Msg {
    private GuildMessageReceivedEvent event;
    private long msg_id;
    private long tc_id;
    private String content;
    private String timeStamp;

    public Msg(GuildMessageReceivedEvent event, long msg_id, long tc_id, String timeStamp) {
        this.event = event;
        this.msg_id = msg_id;
        this.tc_id = tc_id;
        this.timeStamp = timeStamp;
        content = event.getMessage().getContentRaw();
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    public long getMsg_id() {
        return msg_id;
    }

    public long getTc_id() {
        return tc_id;
    }

    public String getContent() {
        return content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}

