package me.evelyn.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ClearCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("Give me some music to clear first.").queue();
            return;
        }

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessageFormat("Join `%s` before trying to sabotage your server's tunes.", selfVoiceState.getChannel().getName()).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("At least face your enemy when you try to delete their music.").queue();
            return;
        }


        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("There is nothing in the queue, smart one.").queue();
            return;
        }

        musicManager.scheduler.queue.clear();

        channel.sendMessage("All the songs in the queue have **vanished**.").queue();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "Clears the entire queue";
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }

    @Override
    public List<String> getAliases() {
        return List.of("clear", "cl");
    }
}
