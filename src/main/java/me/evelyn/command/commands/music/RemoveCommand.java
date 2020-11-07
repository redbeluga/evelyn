package me.evelyn.command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static me.evelyn.command.HelperMethods.isInteger;

public class RemoveCommand implements ICommand {
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
            channel.sendMessageFormat("Get in `%s` first before you try to mess it up it.", selfVoiceState.getChannel().getName()).queue();
            return;
        }

        List<String> input = ctx.getArgs();
        if(input.size() < 1 || !isInteger(input.get(0))){
            channel.sendMessage("What am I going to remove?").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.size() == 0){
            channel.sendMessage("There is nothing to remove from the queue").queue();
            return;
        }
        if(queue.size() < Integer.parseInt(input.get(0))){
            channel.sendMessage("The queue isn't that big. Add some more songs first.").queue();
            return;
        }
        int removedSongIndex = Integer.parseInt(input.get(0));

        final BlockingQueue<AudioTrack> dummyQueue = musicManager.scheduler.queue;
        AudioTrack removedTrack = null;
        try {
            int count = 0;
            for(AudioTrack track : dummyQueue){
                if(count+1 == removedSongIndex){
                    removedTrack = track;
                }
                count += 1;
            }
            musicManager.scheduler.queue.remove(removedTrack);
        } catch (NullPointerException | ClassCastException n){
            channel.sendMessage("I couldn't remove it for some reason. Try again?");
        }
        channel.sendMessageFormat("**Removed:** `%s` by `%s`", removedTrack.getInfo().title, removedTrack.getInfo().author).queue();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public List<String> getAliases() {
        return List.of("remove", "delete", "rm");
    }
}
