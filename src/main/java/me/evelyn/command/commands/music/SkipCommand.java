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

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("What am I going to skip if I'm not even playing music right now.").queue();
            return;
        }

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessageFormat("Get in `%s` first before you try to hijack it.", selfVoiceState.getChannel().getName()).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessageFormat("Join `%s` before trying to sabotage your server's tunes.", selfVoiceState.getChannel().getName()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("There is nothing to skip, smart one.").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        channel.sendMessage("**Skipping**").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public List<String> getAliases() {
        return List.of("skip", "s", "next");
    }
}
