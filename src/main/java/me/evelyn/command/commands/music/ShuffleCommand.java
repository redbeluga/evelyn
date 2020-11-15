package me.evelyn.command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ShuffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I'm not even in a voice channel. Stop bothering me.").queue();
            return;
        }
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if(!memberVoiceState.inVoiceChannel() || memberVoiceState.getChannel() != selfVoiceState.getChannel()){
            channel.sendMessageFormat("Get in `%s` first before you try to mess with it.", selfVoiceState.getChannel().getName()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.size() == 0){
            channel.sendMessage("There is nothing to shuffle. The queue is completely empty.").queue();
            return;
        }

        // Shuffle
        shuffle(musicManager.scheduler.queue, ctx);

        channel.sendMessage("**Shuffled**").queue();
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public List<String> getAliases() {
        return List.of("shuffle", "random");
    }

    private void shuffle(BlockingQueue<AudioTrack> queue, CommandContext ctx){
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        List<AudioTrack> shuffledqueue = new ArrayList<>(queue);

        Collections.shuffle(shuffledqueue);

        musicManager.scheduler.queue.clear();

        for(AudioTrack track : shuffledqueue){
            musicManager.scheduler.queue(track);
        }
    }
}
