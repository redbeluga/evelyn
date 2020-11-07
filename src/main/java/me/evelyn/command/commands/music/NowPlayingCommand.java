package me.evelyn.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class NowPlayingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("I'm not even in a voice channel. What music can I play?").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        if(track == null){
            channel.sendMessage("It's completely silent right now.").queue();
            return;
        }

        final AudioTrackInfo info = track.getInfo();

        channel.sendMessageFormat("Now playing `%s` by `%s`", info.title, info.author).queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public List<String> getAliases() {
        return List.of("nowplaying", "np", "currentlyplaying");
    }
}
