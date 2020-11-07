package me.evelyn.command.commands.music;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import me.evelyn.command.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class DisconnectCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessageFormat("Get in `%s` first before you try to hijack it.", selfVoiceState.getChannel().getName()).queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("What are you going to make me disconnect from, the air?").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessageFormat("Get in `%s` first before you try to hijack it.", selfVoiceState.getChannel().getName()).queue();
            return;
        }
        final TrackScheduler scheduler = musicManager.scheduler;
        scheduler.setLoop(false);

        audioManager.closeAudioConnection();
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        channel.sendMessageFormat("**Disconnecting from ** `%s`", memberChannel.getName()).queue();
    }

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public List<String> getAliases() {
        return List.of("dc", "disconnect", "leave");
    }
}
