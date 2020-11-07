package me.evelyn.command.commands.music;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import me.evelyn.command.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class LoopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I'm not even in a voice channel. Stop bothering me.").queue();
            return;
        }
        if (memberVoiceState.getChannel() == null || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("Come into the voice channel to change the settings.").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final TrackScheduler scheduler = musicManager.scheduler;

        if(scheduler.getLoop()){
            scheduler.setLoop(!scheduler.getLoop());
            channel.sendMessage("**Loop Disabled**").queue();
        }
        else{
            scheduler.setLoop(!scheduler.getLoop());
            channel.sendMessage("**Loop Enabled**").queue();
        }
    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public List<String> getAliases() {
        return List.of("loop", "lq", "queueloop", "loopqueue", "qloop");
    }
}
