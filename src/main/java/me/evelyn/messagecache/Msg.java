package me.evelyn.messagecache;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;

public class Msg {
    private final GuildMessageReceivedEvent event;
    private final long msg_id;
    private final long tc_id;
    private final String content;
    private final Instant timeStamp;

    public Msg(GuildMessageReceivedEvent event, long msg_id, long tc_id, Instant timeStamp) {
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

    public Instant getTimeStamp() {
        return timeStamp;
    }
}

