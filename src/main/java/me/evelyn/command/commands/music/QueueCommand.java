package me.evelyn.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static me.evelyn.command.HelperMethods.formatTime;
import static me.evelyn.command.HelperMethods.isInteger;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I'm not even in a voice channel. Stop bothering me.").queue();
            return;
        }

        List<String> input = ctx.getArgs();

        int pageNum = 1;
        if(input.size() >= 1) {
            if (isInteger(input.get(0)))
                pageNum = Integer.parseInt(input.get(0));
            else {
                channel.sendMessage("Please give an integer. Don't try to break me.").queue();
                return;
            }
        }
        if(pageNum < 1){
            channel.sendMessage("Don't try to break me.").queue();
            return;
        }


        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        final int trackCount = queue.size();
        final int pageCount = 10;
        int totalPageNum = 0;
        if(trackCount%pageCount == 0){
            totalPageNum = trackCount/pageCount;
        }
        else{
            totalPageNum = trackCount/pageCount + 1;
        }

        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(pageNum > totalPageNum && pageNum != 1){
            channel.sendMessage("The queue ain\'t that big").queue();
            return;
        }

        if(audioPlayer.getPlayingTrack() == null && queue.size() == 0) {
            channel.sendMessage("It's dead silent in the voice channel").queue();
            return;
        }

        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("There is nothing playing right now.").queue();
            return;
        }

        final AudioTrack curTrack = audioPlayer.getPlayingTrack();


        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final EmbedBuilder message = new EmbedBuilder()
                .setColor(0xf7003e)
                .setTitle("**Queue for " + ctx.getGuild().getName() + "**");


        final AudioTrackInfo curInfo = curTrack.getInfo();
        String queueMessage = "__Now Playing:__\n" + curInfo.title + " | `" + formatTime(curTrack.getDuration()) + "`\n\n" + "__Up Next:__\n";

        if (totalPageNum != 0) {
            if (pageNum != totalPageNum) {
                if(pageNum != 1){
                    queueMessage = "";
                }
                for (int i = pageCount * (pageNum - 1); i < pageCount * pageNum; i++) {
                    final AudioTrack track = trackList.get(i);
                    final AudioTrackInfo info = track.getInfo();

                    queueMessage = queueMessage + "`" + String.valueOf(i + 1) + ".` " + info.title + " | `"
                            + formatTime(track.getDuration()) + "`\n\n";

                }
            }
            else {
                if(pageNum != 1){
                    queueMessage = "";
                }
                for (int i = pageCount * (pageNum - 1); i < trackCount; i++) {
                    final AudioTrack track = trackList.get(i);
                    final AudioTrackInfo info = track.getInfo();

                    queueMessage = queueMessage + "`" + String.valueOf(i + 1) + ".` " + info.title + " | `"
                            + formatTime(track.getDuration()) + "`\n\n";
                }
            }
        }
        else{
            queueMessage = "__Now Playing:__\n" + curInfo.title + " | `" + formatTime(curTrack.getDuration()) + "`";
            totalPageNum = 1;
        }

        message.setDescription(queueMessage);
        String loopStatus = "Disabled";
        if(musicManager.scheduler.getLoop()){
            loopStatus = "Enabled";
        }
        message.setFooter("Page " + pageNum + "/" + Integer.toString(totalPageNum) + "    Loop " + loopStatus, ctx.getMember().getUser().getAvatarUrl());

        channel.sendMessage(message.build()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("queue", "q");
    }
}
